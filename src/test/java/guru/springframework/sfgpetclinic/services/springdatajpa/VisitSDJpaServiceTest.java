package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        // given
        Visit visit = new Visit();
        Visit visit1 = new Visit();
        Set<Visit> visits = new HashSet<>();
        visits.add(visit);
        visits.add(visit1);
        given(visitRepository.findAll()).willReturn(visits);

        // when
        Set<Visit> foundVisits = service.findAll();

        // then
        then(visitRepository).should().findAll();
        assertThat(foundVisits).hasSize(2);
    }

    @Test
    void findById() {
        // given
        Visit visit = new Visit(1l);
        given(visitRepository.findById(1l)).willReturn(Optional.of(visit));

        // when
        Visit foundVisit = service.findById(1l);

        // then
        then(visitRepository).should().findById(anyLong());
        assertThat(foundVisit).isNotNull();
    }

    @Test
    void save() {
        // given
        Visit visit = new Visit(1l);
        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        // when
        Visit savedVisit = service.save(new Visit());

        // then
        then(visitRepository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        // given
        Visit visit = new Visit();

        // when
        service.delete(visit);

        // then
        then(visitRepository).should().delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        // given - none

        // when
        service.deleteById(1l);

        // then
        then(visitRepository).should().deleteById(1l);
    }
}