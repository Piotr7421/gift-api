package pl.spring.giftapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.spring.giftapi.exceptions.InsertSqlException;
import pl.spring.giftapi.properties.JdbcProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class KidsFileImportService {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcProperties jdbcProperties;

    @Transactional
    public void importKidsFromFile(Path csvPath) throws IOException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(csvPath)) {
            String line;
            int counter = 0;
            long startTime = System.currentTimeMillis();
            Collection<String> lines = new ArrayList<>();
            // Skip the first line (header)
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
                counter++;
                if (counter % jdbcProperties.getBatchSize() == 0) {
                    batchInsert(lines);
                    log.info("{} lines in {}ms", counter, System.currentTimeMillis() - startTime);
                    lines.clear();
                }
            }
            if (!lines.isEmpty()) {
                batchInsert(lines);
                log.info("Remaining {} lines in {}ms", lines.size(), System.currentTimeMillis() - startTime);
            }
        } catch (RuntimeException e) {
            throw new InsertSqlException(MessageFormat.format("Failed to insert data from file {0}", csvPath.getFileName()));
        }
    }

    private void batchInsert(final Collection<String> lines) {
        jdbcTemplate.batchUpdate("INSERT INTO kid ( first_name, last_name, birth_date, version, kid_type ) " +
                        "VALUES ( ?, ?, ?, 0, 'Kid')",
                lines,
                jdbcProperties.getBatchSize(),
                (PreparedStatement ps, String line) -> {
                    String[] params = line.split(",");
                    ps.setString(1, params[0]);
                    ps.setString(2, params[1]);
                    ps.setString(3, params[2]);
                });
    }
}
