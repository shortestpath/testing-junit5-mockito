package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import javafx.beans.binding.MapBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService service;

    @Mock
    Model model;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        given(service.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation -> {
                   List<Owner> owners = new ArrayList<>();
                   String name = invocation.getArgument(0);

                   if (name.equals("%Buck%")) {
                       owners.add(new Owner(1l, "Joe", "Buck"));
                       return owners;
                   } else if (name.equals("%DontFindMe%")) {
                       return owners;
                   } else if (name.equals("%FindMe%")) {
                       owners.add(new Owner(1l, "Joe", "Buck"));
                       owners.add(new Owner(2l, "Joe", "Buck2"));
                       return owners;
                   }

                   throw new RuntimeException("Invalid Argument");
                });
    }

    @Test
    void processFindFormWildcardFound() {
        // given
        Owner owner = new Owner(1l, "Joe", "FindMe");
        InOrder inOrder = inOrder(model, service);

        // when
        String viewName = controller.processFindForm(owner, bindingResult, model);

        // then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);
        inOrder.verify(service).findAllByLastNameLike(anyString());
        inOrder.verify(model, times(1)).addAttribute(anyString(), anyList());
        verifyNoMoreInteractions(model);
    }

    @Test
    void processFindFormWildcardStringAnnotation() {
        // given
        Owner owner = new Owner(1l, "Joe", "Buck");

        // when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        // then
        assertThat("%Buck%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

    @Test
    void processFindFormWildcardNotFound() {
        // given
        Owner owner = new Owner(1l, "Joe", "DontFindMe");

        // when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        // then
        verifyNoMoreInteractions(service);
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }

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