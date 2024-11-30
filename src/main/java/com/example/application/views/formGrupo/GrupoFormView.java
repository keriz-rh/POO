package com.example.application.views.formGrupo;

import java.util.ArrayList;
import java.util.HashSet;

import com.example.application.controlador.GrupoController;
import com.example.application.modelo.Estudiante2;
import com.example.application.modelo.Grupo;
import com.example.application.modelo.Periodo;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Educantrol - Grupos")
@Route(value = "grupo-form", layout = MainLayout.class)
public class GrupoFormView extends Composite<VerticalLayout> {

    private final GrupoController controller;

    // Campos del formulario
    private final TextField nombreField = new TextField("Nombre del Grupo");
    private final TextArea descripcionField = new TextArea("Descripción");
    private final ComboBox<Periodo> periodoField = new ComboBox<>("Periodo");
    private final MultiSelectComboBox<Estudiante2> estudiantesField = new MultiSelectComboBox<>("Estudiantes");

    // Grid principal de grupos
    private final Grid<Grupo> gruposGrid = new Grid<>(Grupo.class, false);

    // Botones para filtrar
    private final Button btnFiltrarPorNombre = new Button("Filtrar por Nombre");
    private final Button btnFiltrarPorPeriodo = new Button("Filtrar por Periodo");
    private final Button btnFiltrarPorEstudiantes = new Button("Filtrar por Cantidad de Estudiantes");
    private final Button btnLimpiarFiltros = new Button("Limpiar Filtros");

    public GrupoFormView(GrupoController controller) {
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

        formLayout2Col.add(nombreField, descripcionField, periodoField, estudiantesField);

        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary, buttonSecondary);

        // Layout para botones de filtro
        HorizontalLayout filterButtons = new HorizontalLayout();
        filterButtons.setSpacing(true);
        filterButtons.add(btnFiltrarPorNombre, btnFiltrarPorPeriodo, 
                        btnFiltrarPorEstudiantes, btnLimpiarFiltros);
        layoutColumn2.add(filterButtons);

        getContent().add(layoutColumn2);

        createGrid();

        buttonPrimary.addClickListener(e -> saveGrupo());
        btnFiltrarPorNombre.addClickListener(e -> filtrarPorNombre());
        btnFiltrarPorPeriodo.addClickListener(e -> filtrarPorPeriodo());
        btnFiltrarPorEstudiantes.addClickListener(e -> filtrarPorCantidadEstudiantes());
        btnLimpiarFiltros.addClickListener(e -> limpiarFiltros());

        configurarEstilosBotones();
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layout.setWidth("100%");
        layout.setMaxWidth("800px");

        title.setText("Información del Grupo");
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
        nombreField.setRequired(true);
        nombreField.setMinLength(2);
        nombreField.setMaxLength(50);

        descripcionField.setMaxLength(500);
        descripcionField.setHeight("100px");

        periodoField.setRequired(true);
        periodoField.setItemLabelGenerator(Periodo::getNombre);
        // Aquí deberías cargar los períodos desde el controlador

        estudiantesField.setItemLabelGenerator(
            estudiante -> estudiante.getNombre() + " " + estudiante.getApellido()
        );
        // Aquí deberías cargar los estudiantes desde el controlador
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
        btnFiltrarPorNombre.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorPeriodo.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorEstudiantes.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    private void createGrid() {
        gruposGrid.addColumn(Grupo::getId).setHeader("ID").setSortable(true);
        gruposGrid.addColumn(Grupo::getNombre).setHeader("Nombre").setSortable(true);
        gruposGrid.addColumn(Grupo::getDescripcion).setHeader("Descripción").setSortable(true);
        gruposGrid.addColumn(grupo -> grupo.getPeriodo().getNombre())
                 .setHeader("Periodo").setSortable(true);
        gruposGrid.addColumn(grupo -> grupo.getEstudiantes().size())
                 .setHeader("Cantidad de Estudiantes").setSortable(true);

        gruposGrid.addColumn(new ComponentRenderer<>(grupo -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(grupo));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteGrupo(grupo));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");

        getContent().add(gruposGrid);
        refreshGrid();
    }

    private void saveGrupo() {
        if (validateForm()) {
            Grupo grupo = new Grupo();
            grupo.setNombre(nombreField.getValue());
            grupo.setDescripcion(descripcionField.getValue());
            grupo.setPeriodo(periodoField.getValue());
            grupo.setEstudiantes(new ArrayList<>(estudiantesField.getSelectedItems()));

            try {
                controller.save(grupo);
                Notification.show("Grupo guardado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                resetFields();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Error al guardar el grupo: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    private boolean validateForm() {
        if (nombreField.isEmpty() || periodoField.isEmpty()) {
            Notification.show("Los campos marcados con * son requeridos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    private void resetFields() {
        nombreField.clear();
        descripcionField.clear();
        periodoField.clear();
        estudiantesField.clear();
    }

    private void refreshGrid() {
        gruposGrid.setItems(controller.findAll());
    }

    private void openEditDialog(Grupo grupo) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nombreEdit = new TextField("Nombre");
        TextArea descripcionEdit = new TextArea("Descripción");
        ComboBox<Periodo> periodoEdit = new ComboBox<>("Periodo");
        MultiSelectComboBox<Estudiante2> estudiantesEdit = new MultiSelectComboBox<>("Estudiantes");

        nombreEdit.setValue(grupo.getNombre());
        descripcionEdit.setValue(grupo.getDescripcion());
        periodoEdit.setValue(grupo.getPeriodo());
        estudiantesEdit.setValue(new HashSet<>(grupo.getEstudiantes()));

        // Configurar los campos igual que en el formulario principal
        periodoEdit.setItemLabelGenerator(Periodo::getNombre);
        estudiantesEdit.setItemLabelGenerator(
            estudiante -> estudiante.getNombre() + " " + estudiante.getApellido()
        );

        formLayout.add(nombreEdit, descripcionEdit, periodoEdit, estudiantesEdit);

        Button saveButton = new Button("Guardar", event -> {
            if (!nombreEdit.isEmpty() && !periodoEdit.isEmpty()) {
                grupo.setNombre(nombreEdit.getValue());
                grupo.setDescripcion(descripcionEdit.getValue());
                grupo.setPeriodo(periodoEdit.getValue());
                grupo.setEstudiantes(new ArrayList<>(estudiantesEdit.getSelectedItems()));

                try {
                    controller.save(grupo);
                    refreshGrid();
                    dialog.close();
                    Notification.show("Grupo actualizado correctamente")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception e) {
                    Notification.show("Error al actualizar el grupo: " + e.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteGrupo(Grupo grupo) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este grupo?"));

        Button confirmButton = new Button("Eliminar", event -> {
            try {
                controller.delete(grupo);
                refreshGrid();
                Notification.show("Grupo eliminado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Error al eliminar el grupo: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorNombre() {
        String nombre = nombreField.getValue();
        if (!nombre.isEmpty()) {
            gruposGrid.setItems(controller.findByNombreContainingIgnoreCase(nombre));
        } else {
            Notification.show("Ingrese un nombre para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void filtrarPorPeriodo() {
        Periodo periodo = periodoField.getValue();
        if (periodo != null) {
            gruposGrid.setItems(controller.findByPeriodo(periodo));
        } else {
            Notification.show("Seleccione un periodo para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void filtrarPorCantidadEstudiantes() {
        try {
            int minEstudiantes = 5; // Este valor podría ser configurable
            gruposGrid.setItems(controller.findGruposConMinEstudiantes(minEstudiantes));
            Notification.show("Mostrando grupos con " + minEstudiantes + " o más estudiantes")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Error al filtrar por cantidad de estudiantes")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void limpiarFiltros() {
        resetFields();
        refreshGrid();
        Notification.show("Filtros eliminados")
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}