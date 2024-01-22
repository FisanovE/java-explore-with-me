package ru.practicum.ewm.admin_api.categories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.admin_api.categories.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {

}
