package com.optus.infosec;

import com.optus.infosec.api.dto.EngagementsByTemporal;
import com.optus.infosec.api.service.EngagementService;
import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.enums.Status;
import com.optus.infosec.repositories.EngagementEntityRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class EngagementServiceTest {

    @Mock
    EngagementEntityRepository repository;

    @InjectMocks
    EngagementService service;


    @Test
    @Disabled
    void getEngagementsByMonthlyBasis() {
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(
                new EngagementEntity(
                        1L,
                        "projectName",
                        new Date(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "REEUSTED BY",
                        "ASSIGNED TO",
                        Status.OPEN,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Collections.emptySet(),
                        Collections.emptySet()
                )
        ));
        List<EngagementsByTemporal> monthlyBasis = service.getEngagementsByMonthlyBasis();
        monthlyBasis.forEach(System.out::println);
    }

}
