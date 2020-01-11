package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialtySDJpaService service;

    @Test
    void testDeleteByObject() {
        // given
        Speciality speciality = new Speciality();

        // when
        service.delete(speciality);

        // then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findByIdTest() {
        // given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1l)).willReturn(Optional.of(speciality));

        // when
        Speciality foundSpecialty = service.findById(1l);

        // then
        assertThat(foundSpecialty).isNotNull();
        then(specialtyRepository).should(times(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testDeleteById() {
        // given - none

        // when
        service.deleteById(1l);
        service.deleteById(1l);

        // then
        then(specialtyRepository).should(times(2)).deleteById(1l);
    }

    @Test
    void testDeleteByIdAtLeast() {
        // given - none

        // when
        service.deleteById(1l);
        service.deleteById(1l);

        // then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1l);
    }

    @Test
    void testDeleteByIdAtMost() {
        // given - none

        // when
        service.deleteById(1l);
        service.deleteById(1l);

        // then
        then(specialtyRepository).should(atMost(5)).deleteById(1l);
    }

    @Test
    void testDeleteByIdNever() {
        // given - none

        // when
        service.deleteById(1l);
        service.deleteById(1l);

        // then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1l);
        then(specialtyRepository).should(never()).deleteById(5l);
    }

    @Test
    void testDelete() {
        // given - none

        // when
        service.delete(new Speciality());

        // then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));
        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrows() {
        // given
        given(specialtyRepository.findById(1l)).willThrow(new RuntimeException("boom"));

        // when
        assertThrows(RuntimeException.class, () -> specialtyRepository.findById(1l));

        // then
        then(specialtyRepository).should().findById(1l);
    }

    @Test
    void testDeleteBDD() {
        // given
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        // when
        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        // then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        // given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1l);

        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        // when
        Speciality returnedSpecialty = service.save(speciality);

        // then
        assertThat(returnedSpecialty.getId()).isEqualTo(1l);
    }

    @Test
    void testSaveLambdaNoMatch() {
        // given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription("Not a match");

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1l);

        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpecialty);

        // when
        Speciality returnedSpecialty = service.save(speciality);

        // then
        assertThat(returnedSpecialty.getId()).isEqualTo(1l);
    }
}