package com.greem.project.ui;

import com.greem.project.domain.ChildDto;
import com.greem.project.service.ChildService;
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

@Route("")
@CssImport("./styles/baby-select-view-styles.css")
public class ChildSelectView extends VerticalLayout {

    private ChildService childService;
    private AddChildForm form;

    public ChildSelectView(ChildService childService) {
        this.childService = childService;

        H1 babyMonitorLogo = new H1("Baby Monitor App");
        H2 selectBabyCaption = new H2("Available babies");

        HorizontalLayout availableBabiesBar = createAvailableChildrenBar(childService);
        Button addNewBabyButton = createAddNewChildButton(childService);

        add(babyMonitorLogo, selectBabyCaption, availableBabiesBar, addNewBabyButton);
        setHeightFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private HorizontalLayout createAvailableChildrenBar(ChildService childService) {
        HorizontalLayout availableBabiesBar = new HorizontalLayout();

        for (ChildDto childDto: childService.getAllChildren()) {
            availableBabiesBar.add(createBabySelectionButton(childDto));
        }

        return availableBabiesBar;
    }

    private VerticalLayout createBabySelectionButton(ChildDto childDto) {
        HorizontalLayout functionButtonsBar = new HorizontalLayout();

        Button selectChildButton = new Button(childDto.getFirstName() + " " + childDto.getLastName());
        selectChildButton.addClassName("select-child-button");

        Button deleteChildButton = new Button("", new Icon(VaadinIcon.DEL));
        deleteChildButton.addClassName("delete-child-button");

        functionButtonsBar.add(deleteChildButton);

        VerticalLayout childSelectDeleteBlock = new VerticalLayout();
        childSelectDeleteBlock.add(selectChildButton, functionButtonsBar);

        return childSelectDeleteBlock;
    }
    private Button createAddNewChildButton(ChildService childService) {
        Button addNewBabyButton = new Button("", new Icon(VaadinIcon.PLUS));

        addNewBabyButton.addClickListener(e -> {
            createAddChildDialogWindow(childService);
        });

        addNewBabyButton.addClassName("add-new-baby-button");

        return addNewBabyButton;
    }

    private void createAddChildDialogWindow(ChildService childService) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout content = new VerticalLayout();
        content.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        form = new AddChildForm();
        content.add(new H3("Add new child"), form);

        form.addListener(AddChildForm.SaveEvent.class, e -> {
            addChild(e);
            dialog.close();
        });
        form.addListener(AddChildForm.CancelEvent.class, e -> dialog.close());


        dialog.add(content);

        dialog.open();
    }

    private void addChild(AddChildForm.SaveEvent evt) {
        childService.addChild(evt.getChildDto());
    }


}
