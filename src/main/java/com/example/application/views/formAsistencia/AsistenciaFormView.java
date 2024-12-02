package com.example.application.views.formAsistencia;

import com.example.application.modelo.Asistencia;
import com.example.application.modelo.AsistenciaRepository;
import com.example.application.modelo.Estudiante2;
import com.example.application.modelo.Estudiante2Repository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import com.example.application.views.MainLayout;

import java.time.LocalDate;

@Route("form-asistencia")
public class AsistenciaFormView extends MainLayout {

    private final AsistenciaRepository asistenciaRepository;
    private final Estudiante2Repository estudiante2Repository;

    private ComboBox<Estudiante2> estudianteComboBox;
    private DatePicker fechaDatePicker;
    private RadioButtonGroup<String> estadoRadioGroup;
    private Button guardarButton;

    public AsistenciaFormView(AsistenciaRepository asistenciaRepository, Estudiante2Repository estudiante2Repository) {
        this.asistenciaRepository = asistenciaRepository;
        this.estudiante2Repository = estudiante2Repository;

        // Inicializar los componentes
        estudianteComboBox = new ComboBox<>("Seleccionar Estudiante");
        fechaDatePicker = new DatePicker("Fecha de Asistencia");
        estadoRadioGroup = new RadioButtonGroup<>("Estado de Asistencia");
        guardarButton = new Button("Guardar");

        // Configurar ComboBox con los estudiantes
        estudianteComboBox.setItems(estudiante2Repository.findAll());
        estudianteComboBox.setItemLabelGenerator(Estudiante2::getNombre);

        // Configurar RadioButtonGroup para el estado de asistencia
        estadoRadioGroup.setItems("Presente", "Ausente");

        // Establecer la fecha por defecto al día de hoy
        fechaDatePicker.setValue(LocalDate.now());

        // Configurar el botón de guardar
        guardarButton.addClickListener(event -> guardarAsistencia());

        // Layout del formulario
        FormLayout formLayout = new FormLayout();
        formLayout.add(estudianteComboBox, fechaDatePicker, estadoRadioGroup, guardarButton);
        formLayout.setWidth("100%"); // Asegura que el formulario ocupe todo el ancho disponible
        formLayout.setMaxWidth("600px"); // Limita el ancho máximo del formulario

        // Personalización para mejorar la apariencia
        formLayout.addClassName("form-layout");  // Añadir clase de estilo

        // Crear un VerticalLayout para centrar el formulario
        VerticalLayout verticalLayout = new VerticalLayout(formLayout);
        verticalLayout.setAlignItems(Alignment.CENTER); // Centra el formulario
        verticalLayout.setPadding(true);  // Añadir padding alrededor del formulario
        verticalLayout.setMargin(true);  // Aseguramos que haya margen
        verticalLayout.setWidth("100%"); // Asegura que ocupe todo el ancho disponible

        // Establecer el contenido principal del layout
        setContent(verticalLayout);
    }

    private void guardarAsistencia() {
        Estudiante2 estudiante = estudianteComboBox.getValue();
        LocalDate fecha = fechaDatePicker.getValue();
        String estado = estadoRadioGroup.getValue();

        if (estado == null || estudiante == null) {
            Notification.show("Por favor, completa todos los campos.");
            return;
        }

        boolean presente = estado.equals("Presente");
        Asistencia asistencia = new Asistencia(estudiante, null, fecha, presente);
        asistenciaRepository.save(asistencia);

        Notification.show("Asistencia registrada correctamente.");
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        estudianteComboBox.clear();
        estadoRadioGroup.clear();
        fechaDatePicker.setValue(LocalDate.now());
    }
}
