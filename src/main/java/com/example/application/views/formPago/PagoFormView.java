package com.example.application.views.formPago;

import com.example.application.controlador.PagoController;
import com.example.application.modelo.Pago;
import com.example.application.modelo.Pago.FormaPago;
import com.example.application.modelo.Estudiante2;
import com.example.application.modelo.Estudiante2Repository;
import com.example.application.views.MainLayout;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@PageTitle("Educantrol - Pagos")
@Route(value = "pago-form", layout = MainLayout.class)
public class PagoFormView extends Composite<VerticalLayout> {
    
    private final PagoController pagoController;
    private final Estudiante2Repository estudianteRepository;
    
    private final Grid<Pago> grid = new Grid<>(Pago.class, false);
    private final DatePicker fechaPicker = new DatePicker("Fecha de Pago");
    private final BigDecimalField montoField = new BigDecimalField("Monto");
    private final TextField conceptoField = new TextField("Concepto");
    private final ComboBox<FormaPago> formaPagoCombo = new ComboBox<>("Forma de Pago");
    private final TextField estudianteField = new TextField("Carnet del Estudiante");
    private final Span estudianteInfo = new Span();
    private Estudiante2 selectedEstudiante;

    public PagoFormView(
        PagoController pagoController, 
        @Qualifier("estudiante2Repository") Estudiante2Repository estudianteRepository
    ) {
        this.pagoController = pagoController;
        this.estudianteRepository = estudianteRepository;
        
        configurarLayout();
        configurarComponentes();
        configurarGrid();
        configurarBusquedaEstudiante();
        
        refreshGrid();
    }

    private void configurarLayout() {
        VerticalLayout mainLayout = getContent();
        mainLayout.setWidth("100%");
        mainLayout.setHeightFull();

        H3 titulo = new H3("Registro de Pagos");
        FormLayout formLayout = new FormLayout();
        
        formLayout.add(
            fechaPicker,
            montoField,
            conceptoField,
            formaPagoCombo,
            estudianteField,
            estudianteInfo
        );

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button saveButton = new Button("Guardar", e -> savePago());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancelar", e -> resetForm());
        buttonLayout.add(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        mainLayout.add(titulo, formLayout, buttonLayout, grid);
        mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, titulo);
        mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, formLayout);
        mainLayout.setHorizontalComponentAlignment(Alignment.CENTER, buttonLayout);
    }

    private void configurarComponentes() {
        // DatePicker
        fechaPicker.setValue(LocalDate.now());
        fechaPicker.setMax(LocalDate.now());
        fechaPicker.setRequired(true);

        // MontoField
        montoField.setPrefixComponent(new Span("$"));
        montoField.setValue(BigDecimal.ZERO);
        montoField.setRequiredIndicatorVisible(true);

        // ConceptoField
        conceptoField.setRequired(true);
        conceptoField.setRequiredIndicatorVisible(true);

        // FormaPagoCombo
        formaPagoCombo.setItems(FormaPago.values());
        formaPagoCombo.setItemLabelGenerator(FormaPago::name);
        formaPagoCombo.setRequired(true);
        formaPagoCombo.setRequiredIndicatorVisible(true);

        // EstudianteField
        estudianteField.setPlaceholder("Ingrese el carnet del estudiante");
        estudianteField.setClearButtonVisible(true);
        estudianteField.setRequired(true);
        estudianteField.setRequiredIndicatorVisible(true);

        // EstudianteInfo
        estudianteInfo.getStyle().set("margin-left", "1em");
    }

    private void configurarBusquedaEstudiante() {
        estudianteField.addValueChangeListener(event -> {
            String carnet = event.getValue();
            if (carnet != null && !carnet.trim().isEmpty()) {
                buscarEstudiante(carnet.trim());
            } else {
                estudianteInfo.setText("");
                selectedEstudiante = null;
            }
        });
    }

    private void configurarGrid() {
        grid.addColumn(pago -> pago.getFechaPago().toLocalDate())
            .setHeader("Fecha")
            .setSortable(true);
            
        grid.addColumn(pago -> String.format("$%.2f", pago.getMonto()))
            .setHeader("Monto")
            .setSortable(true);
            
        grid.addColumn(Pago::getConcepto)
            .setHeader("Concepto")
            .setSortable(true);
            
        grid.addColumn(Pago::getFormaPago)
            .setHeader("Forma de Pago")
            .setSortable(true);
            
        grid.addColumn(pago -> Optional.ofNullable(pago.getEstudiante())
        .map(Estudiante2::getCarnet)
        .orElse(""))
        .setHeader("Carnet")
        .setSortable(true);
            
        grid.addColumn(pago -> Optional.ofNullable(pago.getEstudiante())
        .map(e -> e.getNombre() + " " + e.getApellido())
        .orElse(""))
        .setHeader("Estudiante")
        .setSortable(true);

        grid.addColumn(new ComponentRenderer<>(this::createActionButtons))
            .setHeader("Acciones")
            .setTextAlign(ColumnTextAlign.CENTER);

        grid.setWidthFull();
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout createActionButtons(Pago pago) {
        Button editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(e -> openEditDialog(pago));
            
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> openDeleteDialog(pago));
            
        HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
        actions.setSpacing(true);
        return actions;
    }

    private void buscarEstudiante(String carnet) {
        try {
            Optional<Estudiante2> estudiante = estudianteRepository.findByCarnet(carnet);
            if (estudiante.isPresent()) {
                selectedEstudiante = estudiante.get();
                estudianteInfo.setText("Estudiante encontrado: " + 
                    selectedEstudiante.getNombre() + " " + 
                    selectedEstudiante.getApellido());
                estudianteInfo.getStyle().set("color", "var(--lumo-success-color)");
            } else {
                selectedEstudiante = null;
                estudianteInfo.setText("No se encontró ningún estudiante con ese carnet");
                estudianteInfo.getStyle().set("color", "var(--lumo-error-color)");
            }
        } catch (Exception e) {
            Notification.show("Error al buscar estudiante: " + e.getMessage(),
                3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void savePago() {
        try {
            if (!validateForm()) {
                return;
            }

            Pago pago = new Pago();
            pago.setFechaPago(LocalDateTime.of(fechaPicker.getValue(), LocalDateTime.now().toLocalTime()));
            pago.setMonto(montoField.getValue());
            pago.setConcepto(conceptoField.getValue());
            pago.setFormaPago(formaPagoCombo.getValue());
            pago.setEstudiante(selectedEstudiante);

            pagoController.save(pago);
            
            Notification.show("Pago registrado exitosamente",
                3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            resetForm();
            refreshGrid();
            
        } catch (Exception e) {
            Notification.show("Error al guardar el pago: " + e.getMessage(),
                3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private boolean validateForm() {
        List<String> errors = new ArrayList<>();

        if (fechaPicker.isEmpty()) {
            errors.add("La fecha es requerida");
        }
        if (montoField.isEmpty() || montoField.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("El monto debe ser mayor a 0");
        }
        if (conceptoField.isEmpty()) {
            errors.add("El concepto es requerido");
        }
        if (formaPagoCombo.isEmpty()) {
            errors.add("La forma de pago es requerida");
        }
        if (selectedEstudiante == null) {
            errors.add("Debe seleccionar un estudiante válido");
        }

        if (!errors.isEmpty()) {
            Notification.show(String.join("\n", errors),
                5000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    private void resetForm() {
        fechaPicker.setValue(LocalDate.now());
        montoField.setValue(BigDecimal.ZERO);
        conceptoField.clear();
        formaPagoCombo.clear();
        estudianteField.clear();
        estudianteInfo.setText("");
        selectedEstudiante = null;
    }

    private void refreshGrid() {
        try {
            grid.setItems(pagoController.findAll());
        } catch (Exception e) {
            Notification.show("Error al cargar los pagos: " + e.getMessage(),
                3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void openEditDialog(Pago pago) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Editar Pago");

        FormLayout formLayout = new FormLayout();
        
        DatePicker fechaEdit = new DatePicker("Fecha de Pago");
        fechaEdit.setValue(pago.getFechaPago().toLocalDate());
        
        BigDecimalField montoEdit = new BigDecimalField("Monto");
        montoEdit.setValue(pago.getMonto());
        
        TextField conceptoEdit = new TextField("Concepto");
        conceptoEdit.setValue(pago.getConcepto());
        
        ComboBox<FormaPago> formaPagoEdit = new ComboBox<>("Forma de Pago");
        formaPagoEdit.setItems(FormaPago.values());
        formaPagoEdit.setValue(pago.getFormaPago());
        
        TextField estudianteEdit = new TextField("Carnet del Estudiante");
        estudianteEdit.setValue(pago.getEstudiante().getCarnet());
        
        Span estudianteInfoEdit = new Span();
        estudianteInfoEdit.setText("Estudiante: " + 
            pago.getEstudiante().getNombre() + " " + 
            pago.getEstudiante().getApellido());

        formLayout.add(fechaEdit, montoEdit, conceptoEdit, formaPagoEdit, 
                      estudianteEdit, estudianteInfoEdit);

        Button saveButton = new Button("Guardar", event -> {
            try {
                pago.setFechaPago(LocalDateTime.of(fechaEdit.getValue(), 
                    LocalDateTime.now().toLocalTime()));
                pago.setMonto(montoEdit.getValue());
                pago.setConcepto(conceptoEdit.getValue());
                pago.setFormaPago(formaPagoEdit.getValue());
                
                pagoController.save(pago);
                dialog.close();
                refreshGrid();
                
                Notification.show("Pago actualizado correctamente",
                    3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e) {
                Notification.show("Error al actualizar el pago: " + e.getMessage(),
                    3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(formLayout, buttons);
        dialog.open();
    }

    private void openDeleteDialog(Pago pago) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar eliminación");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(new Text("¿Está seguro que desea eliminar este pago?"));
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        Button deleteButton = new Button("Eliminar", event -> {
            try {
                pagoController.delete(pago);
                refreshGrid();
                dialog.close();
                
                Notification.show("Pago eliminado correctamente",
                    3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e) {
                Notification.show("Error al eliminar el pago: " + e.getMessage(),
                    3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(deleteButton, cancelButton);
        buttons.setJustifyContentMode(JustifyContentMode.END);
        buttons.setSpacing(true);

        dialogLayout.add(buttons);
        dialog.add(dialogLayout);
        dialog.open();
    }
}