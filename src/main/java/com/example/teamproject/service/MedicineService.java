package com.example.teamproject.service;

import com.example.teamproject.dto.ApiResponseDto;
import com.example.teamproject.dto.MedicineResultDto;
import com.example.teamproject.dto.MyMedicineDto;
import com.example.teamproject.entity.Medicine;
import com.example.teamproject.entity.User;
import com.example.teamproject.repository.MedicineRepository;
import com.example.teamproject.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.tomcat.util.buf.UriUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${secret.key}")
    private String SERVICE_KEY;

    @Value("${api.url}")
    private String API_URL;

    @Value("${api.path}")
    private String API_PATH;

    public List<MedicineResultDto> searchMedicine(String query, int no, String sort) throws Exception {
        String fullUrl = API_URL + API_PATH + "?ServiceKey=" + SERVICE_KEY +
                "&type=json&numOfRows=10&itemName=" + query +
                "&pageNo=" + no;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(fullUrl);

            httpGet.addHeader("Accept", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getCode() != 200) {
                    throw new RuntimeException("Failed : HTTP Error code : " + response.getCode());
                }
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponseDto responseDto = objectMapper.readValue(responseBody, ApiResponseDto.class);

                List<MedicineResultDto> items = responseDto.getBody().getItems();
                if(sort.equals("up")) {
                    Collections.sort(items, (m1, m2) -> m1.getItemName().compareTo(m2.getItemName()));
                } else {
                    Collections.sort(items, (m1, m2) -> m2.getItemName().compareTo(m1.getItemName()));
                }
                return items;
            }
        }
    }

    public List<MyMedicineDto> getMedicineListByUserId(Long id) {
        List<Medicine> results = medicineRepository.findByUserId(id);
        return results.stream().map(result -> modelMapper.map(result, MyMedicineDto.class)).collect(Collectors.toList());
    }

    public MyMedicineDto getMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("medicine not found"));
        return modelMapper.map(medicine, MyMedicineDto.class);
    }

    public Long createMedicine(Long id, MedicineResultDto medicineResultDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Medicine medicine = modelMapper.map(medicineResultDto, Medicine.class);
        medicine.setUser(user);
        medicineRepository.save(medicine);
        return medicine.getId();
    }

    public Long deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
        return id;
    }



}
