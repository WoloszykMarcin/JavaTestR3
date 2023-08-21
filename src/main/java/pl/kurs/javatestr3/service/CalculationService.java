package pl.kurs.javatestr3.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private final JdbcTemplate jdbcTemplate;

    public CalculationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public double fetchCalculatedAreaFromView(String shapeType, Long shapeId) {
        String sql = "SELECT area FROM " + shapeType.toLowerCase() + "_area_view WHERE id = ?";
        System.out.println("Executing SQL: " + sql);
        return jdbcTemplate.queryForObject(sql, Double.class, shapeId);
    }

    public double fetchCalculatedPerimeterFromView(String shapeType, Long shapeId) {
        String sql = "SELECT perimeter FROM " + shapeType.toLowerCase() + "_perimeter_view WHERE id = ?";
        System.out.println("Executing SQL: " + sql);
        return jdbcTemplate.queryForObject(sql, Double.class, shapeId);
    }
}