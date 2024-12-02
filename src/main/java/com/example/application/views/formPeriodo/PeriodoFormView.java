package com.example.application.views.formPeriodo;

import com.example.application.controlador.PeriodoController;
import com.example.application.modelo.Periodo;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.time.LocalDate;
import java.util.Locale;

@PageTitle("Educantrol - Períodos Académicos")
@Route(value = "periodo-form", layout = MainLayout.class)
public class PeriodoFormView extends Composite<VerticalLayout> {
    
    private final PeriodoController controller;
    
    // Campos del formulario
    private final TextField nombreField = new TextField("Nombre del Período");
    private final TextField anioField = new TextField("Año");
    private final DatePicker fechaInicioField = new DatePicker("Fecha de Inicio");
    private final DatePicker fechaFinField = new DatePicker("Fecha de Fin");
    private final Checkbox activoField = new Checkbox("Período Activo");
    
    // Grid principal
    private final Grid<Periodo> periodosGrid = new Grid<>(Periodo.class, false);
    
    // Botones de filtro
    private final Button btnFiltrarPorAnio = new Button("Filtrar por Año");
    private final Button btnMostrarActivos = new Button("Mostrar Períodos Activos");
    private final Button btnFiltrarPorFecha = new Button("Filtrar por Fecha Actual");
    private final Button btnLimpiarFiltros = new Button("Limpiar Filtros");
    
    public PeriodoFormView(PeriodoController controller) {
        this.controller = controller;
        
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        
        configureLayout(layoutColumn2, h3, formLayout2Col, layoutRow);
        configureFields();
        configureButtons(buttonPrimary, buttonSecondary);
        
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        
        formLayout2Col.add(nombreField, anioField, fechaInicioField, fechaFinField, activoField);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary, buttonSecondary);
        
        // Layout para botones de filtro
        HorizontalLayout filterButtons = new HorizontalLayout();
        filterButtons.setSpacing(true);
        filterButtons.add(btnFiltrarPorAnio, btnMostrarActivos, btnFiltrarPorFecha, btnLimpiarFiltros);
        layoutColumn2.add(filterButtons);
        
        getContent().add(layoutColumn2);
        
        createGrid();
        
        // Configurar listeners
        buttonPrimary.addClickListener(e -> savePeriodo());
        btnFiltrarPorAnio.addClickListener(e -> filtrarPorAnio());
        btnMostrarActivos.addClickListener(e -> mostrarPeriodosActivos());
        btnFiltrarPorFecha.addClickListener(e -> filtrarPorFechaActual());
        btnLimpiarFiltros.addClickListener(e -> limpiarFiltros());
        
        // Estilos para botones
        configurarEstilosBotones();
    }
    
    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        
        title.setText("Gestión de Períodos Académicos");
        title.setWidth("100%");
        
        form.setWidth("100%");
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }
    
    private void configureFields() {
        // Configuración del campo nombre
        nombreField.setRequired(true);
        nombreField.setValueChangeMode(ValueChangeMode.EAGER);
        nombreField.setMinLength(2);
        nombreField.setMaxLength(50);
        
        // Configuración del campo año
        anioField.setRequired(true);
        anioField.setPattern("\\d{4}");
        anioField.setMaxLength(4);
        anioField.setHelperText("Ingrese el año en formato YYYY");
        
        // Configuración de los campos de fecha
        Locale spanishLocale = new Locale("es");
        fechaInicioField.setLocale(spanishLocale);
        fechaFinField.setLocale(spanishLocale);
        
        fechaInicioField.setRequired(true);
        fechaFinField.setRequired(true);
        
        // Validación de fechas
        fechaFinField.addValueChangeListener(event -> {
            if (fechaInicioField.getValue() != null && event.getValue() != null) {
                if (event.getValue().isBefore(fechaInicioField.getValue())) {
                    Notification.show("La fecha de fin no puede ser anterior a la fecha de inicio")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    fechaFinField.clear();
                }
            }
        });
    }
    
    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
        cancel.addClickListener(e -> resetFields());
    }
    
    private void configurarEstilosBotones() {
        btnFiltrarPorAnio.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnMostrarActivos.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnFiltrarPorFecha.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }
    
    private void createGrid() {
        periodosGrid.addColumn(Periodo::getId).setHeader("ID").setSortable(true);
        periodosGrid.addColumn(Periodo::getNombre).setHeader("Nombre").setSortable(true);
        periodosGrid.addColumn(Periodo::getAnio).setHeader("Año").setSortable(true);
        periodosGrid.addColumn(Periodo::getFechaInicio).setHeader("Fecha Inicio").setSortable(true);
        periodosGrid.addColumn(Periodo::getFechaFin).setHeader("Fecha Fin").setSortable(true);
        periodosGrid.addColumn(Periodo::getActivo).setHeader("Activo").setSortable(true);
        
        periodosGrid.addColumn(new ComponentRenderer<>(periodo -> {
            HorizontalLayout layout = new HorizontalLayout();
            
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(periodo));
            
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deletePeriodo(periodo));
            
            Button activateButton = new Button(new Icon(VaadinIcon.POWER_OFF));
            activateButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            activateButton.addClickListener(e -> activarPeriodo(periodo));
            
            layout.add(editButton, deleteButton, activateButton);
            return layout;
        })).setHeader("Acciones");
        
        getContent().add(periodosGrid);
        refreshGrid();
    }
    
    private void savePeriodo() {
        if (validateFields()) {
            Periodo periodo = new Periodo();
            periodo.setNombre(nombreField.getValue());
            periodo.setAnio(anioField.getValue());
            periodo.setFechaInicio(fechaInicioField.getValue());
            periodo.setFechaFin(fechaFinField.getValue());
            periodo.setActivo(activoField.getValue());
            
            try {
                controller.save(periodo);
                Notification.show("Período guardado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                resetFields();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Error al guardar el período: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }
    
    private boolean validateFields() {
        if (nombreField.isEmpty() || anioField.isEmpty() || 
            fechaInicioField.isEmpty() || fechaFinField.isEmpty()) {
            Notification.show("Todos los campos son requeridos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        
        if (!anioField.getValue().matches("\\d{4}")) {
            Notification.show("El año debe tener 4 dígitos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        
        return true;
    }
    
    private void resetFields() {
        nombreField.clear();
        anioField.clear();
        fechaInicioField.clear();
        fechaFinField.clear();
        activoField.clear();
    }
    
    private void refreshGrid() {
        periodosGrid.setItems(controller.findAll());
    }
    
    private void openEditDialog(Periodo periodo) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        
        TextField nombreEdit = new TextField("Nombre");
        TextField anioEdit = new TextField("Año");
        DatePicker fechaInicioEdit = new DatePicker("Fecha de Inicio");
        DatePicker fechaFinEdit = new DatePicker("Fecha de Fin");
        Checkbox activoEdit = new Checkbox("Activo");
        
        nombreEdit.setValue(periodo.getNombre());
        anioEdit.setValue(periodo.getAnio());
        fechaInicioEdit.setValue(periodo.getFechaInicio());
        fechaFinEdit.setValue(periodo.getFechaFin());
        activoEdit.setValue(periodo.getActivo());
        
        formLayout.add(nombreEdit, anioEdit, fechaInicioEdit, fechaFinEdit, activoEdit);
        
        Button saveButton = new Button("Guardar", event -> {
            if (!nombreEdit.isEmpty() && !anioEdit.isEmpty() && 
                !fechaInicioEdit.isEmpty() && !fechaFinEdit.isEmpty()) {
                
                periodo.setNombre(nombreEdit.getValue());
                periodo.setAnio(anioEdit.getValue());
                periodo.setFechaInicio(fechaInicioEdit.getValue());
                periodo.setFechaFin(fechaFinEdit.getValue());
                periodo.setActivo(activoEdit.getValue());
                
                try {
                    controller.save(periodo);
                    refreshGrid();
                    dialog.close();
                    Notification.show("Período actualizado correctamente")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception e) {
                    Notification.show("Error al actualizar el período: " + e.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
        
        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        
        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }
    
    private void deletePeriodo(Periodo periodo) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este período?"));
        
        Button confirmButton = new Button("Eliminar", event -> {
            try {
                controller.delete(periodo);
                refreshGrid();
                Notification.show("Período eliminado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Error al eliminar el período: El período está siendo utilizado")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());
        
        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }
    
    private void activarPeriodo(Periodo periodo) {
        try {
            controller.activarPeriodo(periodo.getId());
            refreshGrid();
            Notification.show("Período activado correctamente")
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e) {
                Notification.show("Error al activar el período: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    
    private void filtrarPorAnio() {
        String anio = anioField.getValue();
        if (!anio.isEmpty()) {
            if (anio.matches("\\d{4}")) {
                periodosGrid.setItems(controller.findByAnio(anio));
            } else {
                Notification.show("El año debe tener 4 dígitos")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else {
            Notification.show("Ingrese un año para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    private void mostrarPeriodosActivos() {
        periodosGrid.setItems(controller.findPeriodosActivos());
        Notification.show("Mostrando períodos activos")
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    
    private void filtrarPorFechaActual() {
        LocalDate fechaActual = LocalDate.now();
        periodosGrid.setItems(controller.findPeriodosPorFecha(fechaActual));
        Notification.show("Mostrando períodos actuales")
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    
    private void limpiarFiltros() {
        resetFields();
        refreshGrid();
        Notification.show("Filtros eliminados")
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}