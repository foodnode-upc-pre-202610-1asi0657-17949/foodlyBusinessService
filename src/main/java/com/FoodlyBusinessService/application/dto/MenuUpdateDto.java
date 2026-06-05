package com.FoodlyBusinessService.application.dto;

import com.FoodlyBusinessService.domain.model.Menu;

public class MenuUpdateDto {
    private Menu menu;

    public MenuUpdateDto() {}

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }
}
