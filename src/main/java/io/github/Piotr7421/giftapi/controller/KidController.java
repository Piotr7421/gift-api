package io.github.Piotr7421.giftapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.github.Piotr7421.giftapi.model.command.CreateGiftCommand;
import io.github.Piotr7421.giftapi.model.command.CreateKidCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateGiftCommand;
import io.github.Piotr7421.giftapi.model.command.UpdateKidCommand;
import io.github.Piotr7421.giftapi.model.dto.GiftDto;
import io.github.Piotr7421.giftapi.model.dto.KidDto;
import io.github.Piotr7421.giftapi.service.GiftService;
import io.github.Piotr7421.giftapi.service.KidService;
import io.github.Piotr7421.giftapi.service.KidsFilePreparationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kids")
public class KidController {

    private final KidService kidService;
    private final GiftService giftService;
    private final KidsFilePreparationService kidsFilePreparationService;

    @GetMapping
    public Page<KidDto> findAll(Pageable pageable) {
        return kidService.findAll(pageable);
    }

    @GetMapping("/{kidId}")
    public KidDto findById(@PathVariable int kidId) {
        return kidService.findById(kidId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KidDto create(@RequestBody @Valid CreateKidCommand command) {
        return kidService.save(command);
    }

    @PutMapping("/{kidId}")
    public KidDto update(@PathVariable int kidId, @RequestBody @Valid UpdateKidCommand command) {
        return kidService.update(kidId, command);
    }

    @DeleteMapping("/{kidId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int kidId) {
        kidService.delete(kidId);
    }

    @GetMapping("/{kidId}/gifts")
    public Page<GiftDto> findAllGifts(@PathVariable int kidId, Pageable pageable) {
        return giftService.findAll(kidId, pageable);
    }

    @GetMapping("/{kidId}/gifts/{giftId}")
    public GiftDto findGiftById(@PathVariable int kidId, @PathVariable int giftId) {
        return giftService.findById(kidId, giftId);
    }

    @PostMapping("/{kidId}/gifts")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftDto createGift(@PathVariable int kidId, @RequestBody @Valid CreateGiftCommand command) {
        return giftService.save(kidId, command);
    }

    @PutMapping("/{kidId}/gifts/{giftId}")
    public GiftDto updateGift(@PathVariable int kidId, @PathVariable int giftId, @RequestBody @Valid UpdateGiftCommand command) {
        return giftService.update(kidId, giftId, command);
    }

    @DeleteMapping("/{kidId}/gifts/{giftId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGift(@PathVariable int kidId, @PathVariable int giftId) {
        giftService.delete(kidId, giftId);
    }

    @PostMapping("/upload")
    public void importKidsFromFileAsync(@RequestParam("file") MultipartFile file) {
        kidsFilePreparationService.processKidsFile(file);
    }
}
