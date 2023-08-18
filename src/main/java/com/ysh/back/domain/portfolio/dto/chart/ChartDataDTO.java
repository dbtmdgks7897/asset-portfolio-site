package com.ysh.back.domain.portfolio.dto.chart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChartDataDTO {
    private List<String> labels;
    private List<DataSetDTO> datasets;

    public static ChartDataDTO of(List<String> labelList, List<BigDecimal> dataList) {
        return ChartDataDTO.builder()
                .labels(labelList)
                .datasets(Arrays.asList(DataSetDTO.setup(dataList)))
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    private static class DataSetDTO {
        private List<String> backgroundColor;
        private List<BigDecimal> data;

        public static DataSetDTO setup(List<BigDecimal> dataList) {
            return DataSetDTO.builder()
                    .backgroundColor(getRandomColors(dataList.size()))
                    .data(dataList)
                    .build();
        }

        private static List<String> getRandomColors(int count) {
            String[] myColors = { "#FFCFDF", "#FEFDCA", "#E0F9B5", "#A5DEE5", "#FAF3F0", "#D4E2D4", "#FFCACC", "#DBC4F0" };
            List<String> colors = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < count; i++) {
                String color = myColors[random.nextInt(myColors.length)];
                colors.add(color);
            }

            return colors;
        }
    }

}
