package dk.cph.dao;

import dk.cph.model.Course;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseDao implements GenericDAO<Course, Integer> {

    private static CourseDao instance;
    private static EntityManagerFactory emf;

    public static CourseDao getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CourseDao();
        }
        return instance;
    }

    @Override
    public List<Course> findAll() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
        }
    }

    @Override
    public void persistEntity(Course entity) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }
    }

    @Override
    public void removeEntity(Integer id) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Course course = em.find(Course.class, id);
            em.remove(course);
            em.getTransaction().commit();
        }
    }

    @Override
    public Course findEntity(Integer id) {
        try (var em = emf.createEntityManager()) {
            return em.find(Course.class, id);
        }
    }

    @Override
    public Course updateEntity(Course c, Integer id) {
        Course merge;
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the course with the given id
            Course course = em.find(Course.class, id);

            // Update the course with the new values
            course.setCourseName(c.getCourseName());
            course.setDescription(c.getDescription());
            course.setStartDate(c.getStartDate());
            course.setEndDate(c.getEndDate());

            // Merge the updated course
            merge = em.merge(course);
            em.getTransaction().commit();
        }
        return merge;
    }
}
