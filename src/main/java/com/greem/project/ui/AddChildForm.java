package com.greem.project.ui;

import com.greem.project.domain.ChildDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;

public class AddChildForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");

    DatePicker dateOfBirthDatePicker = new DatePicker("Date of birth");

    ComboBox<String> genderComboBox = new ComboBox<>("Gender");

    Button save = new Button("Save");
    Button cancel = new Button("Cancel");

    Binder<ChildDto> binder = new Binder<>(ChildDto.class);

    public AddChildForm() {
        addClassName("add-child-form");

        binder.bindInstanceFields(this);
        binder.forField(dateOfBirthDatePicker)
                .withConverter(new LocalDateToStringConverter())
                .bind("dateOfBirth");
        binder.forField(genderComboBox)
                .bind("gender");

        manageSaveButtonAvailability();

        VerticalLayout content = new VerticalLayout();
        content.add(
            configureNameFields(),
            configureDoBAndGenderFields(),
            createActionButtons()
        );
        content.setPadding(false);
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        add(content);
    }

    private Component configureNameFields() {
        firstName.addValueChangeListener(e -> manageSaveButtonAvailability());
        lastName.addValueChangeListener(e -> manageSaveButtonAvailability());
        return new HorizontalLayout(firstName, lastName);
    }

    private Component configureDoBAndGenderFields() {
        return new HorizontalLayout(
                configureDateOfBirthDatePicker(),
                configureGenderComboBox());
    }

    private Component configureDateOfBirthDatePicker() {
        dateOfBirthDatePicker.setPlaceholder("Date of Birth");
        dateOfBirthDatePicker.addValueChangeListener(e -> {
            if (e.getValue().isAfter(LocalDate.now())) {
                dateOfBirthDatePicker.setInvalid(true);
            } else {
                dateOfBirthDatePicker.setInvalid(false);
            }
            manageSaveButtonAvailability();
        });

        return dateOfBirthDatePicker;
    }

    private Component configureGenderComboBox() {
        genderComboBox.setItems("M", "F", "O");
        genderComboBox.setValue("M");
        genderComboBox.setPlaceholder("Gender");
        genderComboBox.setAllowCustomValue(false);

        genderComboBox.addValueChangeListener(e -> manageSaveButtonAvailability());

        return genderComboBox;
    }

    private Component createActionButtons() {
        save.setId("add-child-save-button");
        cancel.setId("add-child-cancel-button");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickShortcut(Key.ENTER);
        save.addClickListener(click -> validateAndSave());

        cancel.addClickShortcut(Key.ESCAPE);
        cancel.addClickListener(click -> fireEvent(new CancelEvent(this, binder.getBean())));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        HorizontalLayout content = new HorizontalLayout(save, cancel);
        content.addClassName("add-child-action-buttons");

        return content;
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    private void manageSaveButtonAvailability() {
        if (
            !firstName.isEmpty() &&
            !lastName.isEmpty() &&
            !dateOfBirthDatePicker.isInvalid() && !dateOfBirthDatePicker.isEmpty() &&
            !genderComboBox.isEmpty()
        ) {
            save.setEnabled(true);
        } else {
            save.setEnabled(false);
        }
    }

    public void setChild(ChildDto childDto) {
        binder.setBean(childDto);
    }

    public static abstract class AddChildFormEvent extends ComponentEvent<AddChildForm> {
        private ChildDto childDto;

        protected AddChildFormEvent(AddChildForm source, ChildDto childDto) {
            super(source, false);
            this.childDto = childDto;
        }

        public ChildDto getChildDto() {
            return childDto;
        }
    }

    public static class SaveEvent extends AddChildFormEvent {
        public SaveEvent(AddChildForm source, ChildDto childDto) {
            super(source, childDto);
        }
    }

    public static class CancelEvent extends AddChildFormEvent {
        public CancelEvent(AddChildForm source, ChildDto childDto) {
            super(source, childDto);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

