package facades;

import entities.Person;
import entities.PersonDTO;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //TODO Remove/Change this before use
    public long getRenameMeCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }

    }

    @Override
    public Person addPerson(Person p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Person deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query;
        try {
            em.getTransaction().begin();
            query = em.createQuery("DELETE FROM Person p WHERE p.id = :id", Person.class);
            query.setParameter("id", id);
            int updated = query.executeUpdate();
            if(updated == 0) {
                throw new PersonNotFoundException("Could not delete, provided id does not exist");
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return null;
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            return new PersonDTO(em.find(Person.class, id));
        } catch(NullPointerException e) {
            throw new PersonNotFoundException("No person with provided id found");
        }finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Person p", PersonDTO.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Person editPerson(Person p) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query;
        try {
            em.getTransaction().begin();
            query = em.createQuery("UPDATE Person p SET p.phone = :phone, "
                    + "p.firstName = :firstName, p.lastName = :lastName WHERE p.id = :id", Person.class);
            query.setParameter("id", p.getId());
            query.setParameter("phone", p.getPhone());
            query.setParameter("firstName", p.getFirstName());
            query.setParameter("lastName", p.getLastName());
            query.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return null;
    }

}
