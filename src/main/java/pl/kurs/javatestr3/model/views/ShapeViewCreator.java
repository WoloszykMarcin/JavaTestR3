package pl.kurs.javatestr3.model.views;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShapeViewCreator {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, ShapeCalculationLogic> shapeCalculationLogics;

    public ShapeViewCreator(JdbcTemplate jdbcTemplate, List<ShapeCalculationLogic> logicBeans) {
        this.jdbcTemplate = jdbcTemplate;
        this.shapeCalculationLogics = new HashMap<>();
        for (ShapeCalculationLogic logic : logicBeans) {
            String shapeType = logic.getClass().getSimpleName().replace("CalculationLogic", "").toUpperCase();
            shapeCalculationLogics.put(shapeType, logic);
        }
    }

    @PostConstruct
    public void createViewsForAllShapes() {
        for (Map.Entry<String, ShapeCalculationLogic> entry : shapeCalculationLogics.entrySet()) {
            String shapeType = entry.getKey();
            ShapeCalculationLogic logic = entry.getValue();
            createAreaView(shapeType, logic.getAreaCalculationLogic());
            createPerimeterView(shapeType, logic.getPerimeterCalculationLogic());
        }
    }

    public void createAreaView(String shapeType, String areaCalculationLogic) {
        String sql = "CREATE VIEW " + shapeType.toLowerCase() + "_area_view AS " +
                "SELECT id, " + areaCalculationLogic + " AS area FROM Shape WHERE type = '" + shapeType + "'";
        jdbcTemplate.execute(sql);
    }

    public void createPerimeterView(String shapeType, String perimeterCalculationLogic) {
        String sql = "CREATE VIEW " + shapeType.toLowerCase() + "_perimeter_view AS " +
                "SELECT id, " + perimeterCalculationLogic + " AS perimeter FROM Shape WHERE type = '" + shapeType + "'";
        jdbcTemplate.execute(sql);
    }
}