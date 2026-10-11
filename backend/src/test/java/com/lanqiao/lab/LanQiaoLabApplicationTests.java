package com.lanqiao.lab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LanQiaoLabApplicationTests {
    @Autowired MockMvc mvc;

    @Test
    void listsOnlyLicensedContentAsAvailable() throws Exception {
        mvc.perform(get("/api/v1/problems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[3].contentAvailable").value(false));
    }

    @Test
    void knowledgeTreeIsAvailable() throws Exception {
        mvc.perform(get("/api/v1/learning/knowledge-tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nodes").isArray());
    }
}
