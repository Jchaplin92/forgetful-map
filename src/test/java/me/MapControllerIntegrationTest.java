package me;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MapControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnStatus() throws Exception {
        this.mockMvc.perform(get("/status")).andDo(print())
                .andExpect(content().json("{\"status\":\"OK\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnMapValues() throws Exception {
        //create map
        this.mockMvc.perform(post("/create-map")
                        .param("size", "4"))
                .andExpect(content().string("previous map was reset\nnew map was created"))
                .andExpect(status().isCreated())
                .andDo(
                        //put an item into the map
                        firstResult -> this.mockMvc.perform(put("/put-in-map")
                                        .param("key", "my_first_key")
                                        .param("content", "my_first_value"))
                                .andExpect(content().json("{\"my_first_key\":\"my_first_value\"}"))
                                .andExpect(status().isCreated())
                                .andDo(
                                        //get the item from the map
                                        secondResult -> this.mockMvc.perform(get("/find-in-map")
                                                        .param("search", "full"))
                                                .andExpect(content().string(String.format("{\"my_first_key\":\"my_first_value\"}"))))
                );
    }
}
