package org.example.quantix;

import org.example.quantix.repository.InventoryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InventoryItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @BeforeEach
    void setUp() {
        inventoryItemRepository.deleteAll();
    }

    @Test
    void shouldCreateInventoryItemSuccessfully() throws Exception {
        String requestBody = """
                {
                  "name": "iPhone 15",
                  "sku": "IPH-15-128",
                  "category": "Electronics",
                  "quantity": 10,
                  "price": 999.99,
                  "supplierName": "Apple Distributor"
                }
                """;

        mockMvc.perform(post("/api/v1/inventory-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.sku").value("IPH-15-128"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void shouldReturnValidationErrorWhenRequestIsInvalid() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "sku": "",
                  "category": "",
                  "quantity": -5,
                  "price": 0,
                  "supplierName": ""
                }
                """;

        mockMvc.perform(post("/api/v1/inventory-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConflictWhenSkuAlreadyExists() throws Exception {
        String requestBody = """
                {
                  "name": "iPhone 15",
                  "sku": "IPH-15-128",
                  "category": "Electronics",
                  "quantity": 10,
                  "price": 999.99,
                  "supplierName": "Apple Distributor"
                }
                """;

        mockMvc.perform(post("/api/v1/inventory-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/inventory-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetInventoryItemByIdSuccessfully() throws Exception {
        String requestBody = """
                {
                  "name": "Samsung S24",
                  "sku": "SMS-S24-256",
                  "category": "Electronics",
                  "quantity": 5,
                  "price": 899.99,
                  "supplierName": "Samsung Distributor"
                }
                """;

        String response = mockMvc.perform(post("/api/v1/inventory-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = extractId(response);

        mockMvc.perform(get("/api/v1/inventory-items/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samsung S24"))
                .andExpect(jsonPath("$.sku").value("SMS-S24-256"));
    }

    @Test
    void shouldReturnNotFoundWhenItemDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/inventory-items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllInventoryItemsSuccessfully() throws Exception {
        createItem("iPhone 15", "IPH-15-128", "Electronics");
        createItem("Office Chair", "CHR-001", "Furniture");

        mockMvc.perform(get("/api/v1/inventory-items?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void shouldSearchInventoryItemsSuccessfully() throws Exception {
        createItem("iPhone 15", "IPH-15-128", "Electronics");
        createItem("Office Chair", "CHR-001", "Furniture");

        mockMvc.perform(get("/api/v1/inventory-items?search=iphone&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("iPhone 15"));
    }

    @Test
    void shouldFilterItemsByCategorySuccessfully() throws Exception {
        createItem("iPhone 15", "IPH-15-128", "Electronics");
        createItem("Office Chair", "CHR-001", "Furniture");

        mockMvc.perform(get("/api/v1/inventory-items?category=Electronics&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].category").value("Electronics"));
    }

    @Test
    void shouldUpdateInventoryItemSuccessfully() throws Exception {
        createItem("iPhone 15", "IPH-15-128", "Electronics");

        Long id = inventoryItemRepository.findAll().get(0).getId();

        String updateBody = """
                {
                  "name": "iPhone 15 Pro",
                  "sku": "IPH-15-PRO-128",
                  "category": "Electronics",
                  "quantity": 7,
                  "price": 1199.99,
                  "supplierName": "Apple Distributor"
                }
                """;

        mockMvc.perform(put("/api/v1/inventory-items/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.sku").value("IPH-15-PRO-128"))
                .andExpect(jsonPath("$.quantity").value(7));
    }

    @Test
    void shouldDeleteInventoryItemSuccessfully() throws Exception {
        createItem("iPhone 15", "IPH-15-128", "Electronics");

        Long id = inventoryItemRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/v1/inventory-items/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/inventory-items/" + id))
                .andExpect(status().isNotFound());
    }

    private void createItem(String name, String sku, String category) throws Exception {
        String requestBody = """
                {
                  "name": "%s",
                  "sku": "%s",
                  "category": "%s",
                  "quantity": 10,
                  "price": 100.00,
                  "supplierName": "Test Supplier"
                }
                """.formatted(name, sku, category);

        mockMvc.perform(post("/api/v1/inventory-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    private Long extractId(String responseBody) {
        String idPart = responseBody.split("\"id\":")[1].split(",")[0];
        return Long.parseLong(idPart.trim());
    }
}