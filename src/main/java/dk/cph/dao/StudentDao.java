package dk.cph.dao;

import dk.cph.model.Student;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentDao implements GenericDAO<Student, Integer> {

    private static StudentDao instance;
    private static EntityManagerFactory emf;

    public static StudentDao getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new StudentDao();
        }
        return instance;
    }

    @Override
    public List<Student> findAll() {
        try (var em = emf.createEntityManager()) {
            return em.createNamedQuery("Student.findAll", Student.class).getResultList();
        }
    }

    @Override
    public void persistEntity(Student entity) {
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
            Student student = em.find(Student.class, id);
            em.remove(student);
            em.getTransaction().commit();
        }
    }

    @Override
    public Student findEntity(Integer id) {
        try (var em = emf.createEntityManager()) {
            return em.find(Student.class, id);
        }
    }

    public Student findEntityByEmail(String email) {
        try (var em = emf.createEntityManager()) {
            return em.createNamedQuery("Student.findByEmail", Student.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }
    }

    @Override
    public Student updateEntity(Student s, Integer id) {
        Student merge;
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the student with the given id
            Student student = em.find(Student.class, id);

            // Update the student's name and email
            student.setName(s.getName());
            student.setEmail(s.getEmail());

            // Merge the updated student
            merge = em.merge(student);
            em.getTransaction().commit();
        }
        return merge;
    }
}
