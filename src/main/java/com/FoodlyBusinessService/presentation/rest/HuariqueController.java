package com.FoodlyBusinessService.presentation.rest;

import com.FoodlyBusinessService.application.dto.HuariqueResponseDto;
import com.FoodlyBusinessService.application.dto.MenuUpdateDto;
import com.FoodlyBusinessService.application.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/huariques")
@Tag(name = "Business", description = "Operaciones de gestión de huariques y menús")
@SecurityScheme(
    name = "BearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class HuariqueController {

    private final BusinessService businessService;

    public HuariqueController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los huariques", description = "Retorna la lista de restaurantes disponibles.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de huariques recuperada",
            content = @Content(schema = @Schema(implementation = HuariqueResponseDto.class)))
    })
    public ResponseEntity<List<HuariqueResponseDto>> getAllHuariques() {
        List<HuariqueResponseDto> huariques = businessService.getAllHuariques();
        return ResponseEntity.ok(huariques);
    }

    @GetMapping("/{id}/menu")
    @Operation(summary = "Obtener el menú de un huarique", description = "Retorna los detalles del menú dado un ID de huarique.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menú encontrado"),
        @ApiResponse(responseCode = "404", description = "Huarique no encontrado")
    })
    public ResponseEntity<HuariqueResponseDto> getMenu(@PathVariable("id") String id) {
        return businessService.getHuariqueMenu(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/menu")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Actualizar menú", description = "Actualiza el menú del huarique. Requiere rol HUARIQUE_ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Menú actualizado correctamente"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para modificar el menú"),
        @ApiResponse(responseCode = "404", description = "Huarique no encontrado")
    })
    public ResponseEntity<String> updateMenu(@PathVariable("id") String id, @RequestBody MenuUpdateDto updateDto, HttpServletRequest request) {
        
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) request.getAttribute("roles");

        if (roles == null || !roles.contains("HUARIQUE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\":\"No tienes los permisos necesarios (Requiere: [HUARIQUE_ADMIN])\"}");
        }

        boolean updated = businessService.updateMenu(id, updateDto);
        if (updated) {
            return ResponseEntity.ok("{\"message\":\"Menú actualizado y evento publicado\"}");
        }
        return ResponseEntity.notFound().build();
    }
}
