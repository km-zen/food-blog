package pl.coderslab.dao;

import pl.coderslab.exception.NotFoundException;
import pl.coderslab.model.Admin;
import pl.coderslab.model.LastPlan;
import pl.coderslab.model.Plan;
import pl.coderslab.utils.DbUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanDao {
    private static final String CREATE_PLAN_QUERY = "INSERT INTO plan(name, description, created, admin_Id) VALUES (?,?,?,?);";
    private static final String READ_PLAN_QUERY = "SELECT * from plan where id = ?;";
    private static final String FIND_ALL_PLANS_QUERY = "SELECT * FROM plan;";
    private static final String DELETE_PLAN_QUERY = "DELETE FROM plan where id = ?;";
    private static final String UPDATE_PLAN_QUERY = "UPDATE	plan SET name = ? , description = ?, created = ?, admin_id = ? WHERE	id = ?;";
    private static final String PLAN_COUNT_QUERY = "SELECT id FROM plan where admin_id = ?;";
    private static final String LAST_PLAN_QUERY = "SELECT day_name.name as day_name, meal_name,  recipe.name as recipe_name, recipe.description as recipe_description\n" +
            "FROM `recipe_plan`\n" +
            "JOIN day_name on day_name.id=day_name_id\n" +
            "JOIN recipe on recipe.id=recipe_id WHERE\n" +
            "recipe_plan.plan_id =  (SELECT MAX(id) from plan WHERE admin_id = ?) \n" +
            "ORDER by day_name.display_order, recipe_plan.display_order;";
    public Plan create(Plan plan) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement insertStm = connection.prepareStatement(CREATE_PLAN_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStm.setString(1, plan.getName());
            insertStm.setString(2, plan.getDescription());
            insertStm.setString(3, plan.getCreated());
            insertStm.setInt(4, plan.getAdminId());
            int result = insertStm.executeUpdate();

            if (result != 1) {
                throw new RuntimeException("Execute update returned " + result);
            }

            try (ResultSet generatedKeys = insertStm.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    plan.setId(generatedKeys.getInt(1));
                    return plan;
                } else {
                    throw new RuntimeException("Generated key was not found");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Plan read(Integer planId) {
        Plan plan = new Plan();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_PLAN_QUERY)
        ) {
            statement.setInt(1, planId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    plan.setId(resultSet.getInt("id"));
                    plan.setName(resultSet.getString("name"));
                    plan.setDescription(resultSet.getString("description"));
                    plan.setCreated(resultSet.getString("created"));
                    plan.setAdminId(resultSet.getInt("admin_Id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plan;
    }

    public List<Plan> findAll() {
        List<Plan> plans = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PLANS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Plan plan = new Plan();
                plan.setId(resultSet.getInt("id"));
                plan.setName(resultSet.getString("name"));
                plan.setDescription(resultSet.getString("description"));
                plan.setCreated(resultSet.getString("created"));
                plan.setAdminId(resultSet.getInt("admin_id"));
                plans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }

    public void delete(Integer planId) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PLAN_QUERY)) {
            statement.setInt(1, planId);
            statement.executeUpdate();

            boolean deleted = statement.execute();
            if (!deleted) {
                throw new NotFoundException("Product not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Plan plan) {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PLAN_QUERY)) {

            statement.setString(1, plan.getName());
            statement.setString(2, plan.getDescription());
            statement.setString(3, plan.getCreated());
            statement.setInt(4, plan.getAdminId());
            statement.setInt(5, plan.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int planCount(Admin admin) {
        int counter = 0;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(PLAN_COUNT_QUERY)
        ) {
            statement.setInt(1, admin.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    counter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    public LastPlan lastPlanPrint(int adminId) {
        LastPlan recipe_plan = new LastPlan();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(LAST_PLAN_QUERY)) {
            statement.setInt(1, adminId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    recipe_plan.setDay_name(resultSet.getString("day_name"));
                    recipe_plan.setMeal_name(resultSet.getString("meal_name"));
                    recipe_plan.setRecipe_name(resultSet.getString("recipe_name"));
                    recipe_plan.setRecipe_description(resultSet.getString("recipe_description"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe_plan;
    }


}