package vttp.batch5.ssf.day15.repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.ssf.day15.models.Contact;

@Repository
public class ContactRepository {

   // DI the RedisTemplate into ContactRepository
   @Autowired
   @Qualifier("redis-object")
   private RedisTemplate<String, Object> template;

   //hset abc1234 name fred
   //hset abc1234 email fred@email.com
   public void insertContact(Contact contact) {

      // ListOperations<String, Object> listOps = template.opsForList();
      // ValueOperations<String, Object> valueOps = template.opsForValue();

      // key serilaizer, key,values
      HashOperations<String, String, Object> hashOps = template.opsForHash();
      // hashOps.put(contact.getId(), "name", contact.getName());
      // hashOps.put(contact.getId(), "email", contact.getEmail());
      // hashOps.put(contact.getId(), "phone", contact.getPhone());

      Map<String, Object> values = new HashMap<>();
      values.put("name", contact.getName());
      values.put("email", contact.getEmail());
      values.put("phone", contact.getPhone());
      hashOps.putAll(contact.getId(), values);

   }

   // Get all IDs (keys) from Redis
   public Set<String> getIDs() {
      // Create a Set to store the keys
      Set<String> keys = new HashSet<>();
      keys.addAll(template.keys("*"));

      return keys;
   }

   public void deleteContact(String id){
      template.delete(id);
   }

   //hgetall id
   public Optional<Contact> getInfos(String id){
      HashOperations<String, String, Object> hashOps = template.opsForHash();
      Map<String,Object> contact = hashOps.entries(id);

      if(contact.isEmpty()){
         return Optional.empty();
      }

      Contact result = new Contact();
      result.setEmail(contact.get("email").toString());
      result.setName(contact.get("name").toString());
      result.setPhone(contact.get("phone").toString());

      return Optional.of(result);

      
   }
   
}
