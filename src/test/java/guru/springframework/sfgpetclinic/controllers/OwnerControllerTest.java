package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import javafx.beans.binding.MapBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService service;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Test
    void processCreationFormHasErrors() {
        // given
        Owner owner = new Owner(1l, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        String viewName = controller.processCreationForm(owner, bindingResult);

        // then
        assertThat(viewName).isEqualToIgnoringCase(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormHasNoErrors() {
        // given
        Owner owner = new Owner(5l, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(false);

        Owner savedOwner = new Owner(5l, "Joe", "Buck");
        given(service.save(owner)).willReturn(owner);

        // when
        String viewName = controller.processCreationForm(owner, bindingResult);

        // then
        verify(service).save(owner);
        assertThat(viewName).isEqualToIgnoringCase(REDIRECT_OWNERS_5);
    }
}