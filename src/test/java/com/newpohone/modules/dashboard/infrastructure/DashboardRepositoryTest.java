package com.newpohone.modules.dashboard.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@ExtendWith(MockitoExtension.class)
class DashboardRepositoryTest {

    @Mock
    private JdbcTemplate jdbc;

    @Test
    void buildsOperationsSummary() {
        when(jdbc.queryForObject(anyString(), eq(Object.class))).thenReturn(10);
        when(jdbc.query(anyString(), org.mockito.ArgumentMatchers.<RowMapper<Map<String, Object>>>any()))
                .thenReturn(List.of(Map.of("nombre", "Celulares", "total", 1000)));

        Map<String, Object> summary = new DashboardRepository(jdbc).getSummary();

        assertEquals(10, summary.get("sales"));
        assertEquals(10, summary.get("orders"));
        assertEquals(10, summary.get("customers"));
        assertEquals(1, ((List<?>) summary.get("salesByCategory")).size());
    }
}
