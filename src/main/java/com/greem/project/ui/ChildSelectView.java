package com.greem.project.ui;

import com.greem.project.backend.BackEndClient;
import com.greem.project.domain.ChildDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@CssImport("./styles/baby-select-view-styles.css")
public class ChildSelectView extends VerticalLayout {

    public ChildSelectView(@Autowired BackEndClient backEndClient) {

        H1 babyMonitorLogo = new H1("Baby Monitor App");
        H2 selectBabyCaption = new H2("Available babies");

        HorizontalLayout availableBabiesBar = createAvailableChildrenBar(backEndClient);
        Button addNewBabyButton = createAddNewChildButton();

        add(babyMonitorLogo, selectBabyCaption, availableBabiesBar, addNewBabyButton);
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private HorizontalLayout createAvailableChildrenBar(BackEndClient backEndClient) {
        HorizontalLayout availableBabiesBar = new HorizontalLayout();

        for (ChildDto childDto: backEndClient.getAllChildren()) {
            availableBabiesBar.add(createBabySelectionButton(childDto));
        }

        return availableBabiesBar;
    }

    private VerticalLayout createBabySelectionButton(ChildDto childDto) {
        HorizontalLayout functionButtonsBar = new HorizontalLayout();

        Button selectChildButton = new Button(childDto.getFullName());
        selectChildButton.addClassName("select-child-button");

        Button deleteChildButton = new Button("", new Icon(VaadinIcon.DEL));
        deleteChildButton.addClassName("delete-child-button");

        functionButtonsBar.add(deleteChildButton);

        VerticalLayout childSelectDeleteBlock = new VerticalLayout();
        childSelectDeleteBlock.add(selectChildButton, functionButtonsBar);

        return childSelectDeleteBlock;
    }
    private Button createAddNewChildButton() {
        Button addNewBabyButton = new Button("", new Icon(VaadinIcon.PLUS));

        addNewBabyButton.addClickListener(e -> {
            createAddChildDialogWindow();
        });

        addNewBabyButton.addClassName("add-new-baby-button");

        return addNewBabyButton;
    }

    private void createAddChildDialogWindow() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout content = new VerticalLayout();
        content.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        AddChildForm form = new AddChildForm();
        content.add(new H3("Add new child"), form);

        Button saveButton = form.getSave();
        Button cancelButton = form.getCancel();

        saveButton.addClickListener(e -> {
           dialog.close();
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
        });

        dialog.add(content);

        dialog.open();
    }

    private Button getButtonById(Component content, String id) {
        return content.getChildren()
                .flatMap(Component::getChildren)
                .filter(component -> component.getId().orElseGet(String::new).equals(id))
                .findFirst()
                .map(component -> (Button) component)
                .orElseThrow(NullPointerException::new);
    }
}
