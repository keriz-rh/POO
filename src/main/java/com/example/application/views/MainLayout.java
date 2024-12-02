package com.example.application.views;

import com.example.application.views.formProfesor.ProfesorFormView;
import com.example.application.views.formEstudiante.EstudianteFormView;
import com.example.application.views.formPago.PagoFormView;
import com.example.application.views.formPeriodo.PeriodoFormView;
import com.example.application.views.formHorario.HorarioFormView;
import com.example.application.views.formMateria.MateriaFormView;
import com.example.application.views.formGrupo.GrupoFormView;
import com.example.application.views.formAsistencia.AsistenciaFormView; // Nueva vista agregada
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * La vista principal es un contenedor a nivel superior para otras vistas.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("Educantrol");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        // Elementos de navegación existentes
        nav.addItem(new SideNavItem("Profesores", 
            ProfesorFormView.class, 
            LineAwesomeIcon.CHALKBOARD_TEACHER_SOLID.create()));
            
        nav.addItem(new SideNavItem("Estudiantes", EstudianteFormView.class, LineAwesomeIcon.GRADUATION_CAP_SOLID.create()));

        nav.addItem(new SideNavItem("Gestión de Pagos", 
            PagoFormView.class, 
            LineAwesomeIcon.MONEY_BILL_SOLID.create()));  

        nav.addItem(new SideNavItem("Gestión de Materias", 
            MateriaFormView.class, 
            LineAwesomeIcon.BOOK_SOLID.create()));           

        nav.addItem(new SideNavItem("Gestión de Períodos",
            PeriodoFormView.class,
            LineAwesomeIcon.CALENDAR_ALT_SOLID.create()));

        nav.addItem(new SideNavItem("Gestión de Horarios", 
        HorarioFormView.class, 
        LineAwesomeIcon.CALENDAR_ALT_SOLID.create()));


        nav.addItem(new SideNavItem("Gestión de Grupos", 
            GrupoFormView.class, 
            LineAwesomeIcon.USERS_SOLID.create()));

        // Nueva entrada para la vista de Asistencia
        nav.addItem(new SideNavItem("Control de Asistencias", 
            AsistenciaFormView.class, 
            LineAwesomeIcon.CHECK_CIRCLE_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
