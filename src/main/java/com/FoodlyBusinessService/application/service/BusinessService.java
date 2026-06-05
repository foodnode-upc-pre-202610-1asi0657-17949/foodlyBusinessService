package com.FoodlyBusinessService.application.service;

import com.FoodlyBusinessService.application.dto.HuariqueResponseDto;
import com.FoodlyBusinessService.application.dto.MenuUpdateDto;
import com.FoodlyBusinessService.application.dto.MenuUpdatedEventDto;
import com.FoodlyBusinessService.domain.model.Huarique;
import com.FoodlyBusinessService.infrastructure.messaging.publisher.MenuEventPublisher;
import com.FoodlyBusinessService.infrastructure.persistence.HuariqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    private final HuariqueRepository huariqueRepository;
    private final MenuEventPublisher menuEventPublisher;

    public BusinessService(HuariqueRepository huariqueRepository, MenuEventPublisher menuEventPublisher) {
        this.huariqueRepository = huariqueRepository;
        this.menuEventPublisher = menuEventPublisher;
    }

    public List<HuariqueResponseDto> getAllHuariques() {
        return huariqueRepository.findAll()
                .stream()
                .map(HuariqueResponseDto::new)
                .collect(Collectors.toList());
    }

    public Optional<HuariqueResponseDto> getHuariqueMenu(String huariqueId) {
        return huariqueRepository.findById(huariqueId)
                .map(HuariqueResponseDto::new);
    }

    public boolean updateMenu(String huariqueId, MenuUpdateDto updateDto) {
        Optional<Huarique> huariqueOpt = huariqueRepository.findById(huariqueId);
        
        if (huariqueOpt.isEmpty()) {
            return false;
        }

        Huarique huarique = huariqueOpt.get();
        huarique.setMenu(updateDto.getMenu());
        
        huariqueRepository.save(huarique);

        int totalProducts = updateDto.getMenu() != null && updateDto.getMenu().getProducts() != null 
                ? updateDto.getMenu().getProducts().size() : 0;
                
        MenuUpdatedEventDto eventDto = new MenuUpdatedEventDto(huariqueId, totalProducts);
        menuEventPublisher.publishMenuUpdatedEvent(eventDto);

        return true;
    }
}
