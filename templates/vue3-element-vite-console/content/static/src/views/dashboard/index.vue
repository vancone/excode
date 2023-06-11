<template>
  <div class="dashboard">
    <div class="metrics-wrapper">
      <div class="metrics">
        <h1>APIs</h1>
        <span>0</span>
      </div>
      <div class="metrics">
        <h1>Data Stores</h1>
        <span>0</span>
      </div>
      <div class="metrics">
        <h1>Pages</h1>
        <span>0</span>
      </div>
      <div class="metrics">
        <h1>Pages</h1>
        <span>0</span>
      </div>
    </div>
    <div class="chart-wrapper">
      <div class="line-chart-wrapper">
        <h2>Line Chart Demo</h2>
        <div id="line-chart" style="height: 250px"></div>
      </div>
      <div class="pie-chart-wrapper">
        <h2>Pie Chart Demo</h2>
        <div id="pie-chart" style="height: 250px"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted } from "vue";
import * as echarts from 'echarts/core';
import { LineChart, PieChart } from 'echarts/charts';

import {
  TooltipComponent,
  GridComponent,
  TitleComponent,
  LegendComponent,
  MarkLineComponent,
} from 'echarts/components';

import { CanvasRenderer } from 'echarts/renderers';
import { throttle } from "lodash";
import { generateLineChartOptions, generatePieOptions } from "./echarts/options";

echarts.use([
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent,
  MarkLineComponent,
  LineChart,
  PieChart,
  CanvasRenderer,
]);

let lineChart: echarts.ECharts;
let pieChart: echarts.ECharts;

function echartsResize() {
  lineChart.resize();
  pieChart.resize();
}

const throttleEchartResize = throttle(echartsResize, 200);

function fetchLineChartData() {
  const dateList: Array<string> = ['01-01', '01-02', '01-03', '01-04', '01-05'];
  const metric1List: Array<number> = [1, 3, 2, 4, 5];
  const metric2List: Array<number> = [2, 5, 3, 2, 1];

  const lineChartOptions = generateLineChartOptions(
    dateList,
    metric1List,
    metric2List
  );
  lineChart.setOption(lineChartOptions);
}

function fetchPieChartData() {
  const data = {
    a: 20,
    b: 30,
    c: 40,
  };
  const result = Object.entries(data).map(([key, value]) => ({ name: key, value }));
  const options = generatePieOptions(result);
  pieChart.setOption(options);
}

onMounted(() => {
  const elementLineChart = document.getElementById('line-chart');
  const elementPieChart = document.getElementById('pie-chart');
  if (!lineChart && elementLineChart) {
    lineChart = echarts.init(elementLineChart);
  }
  if (!pieChart && elementPieChart) {
    pieChart = echarts.init(elementPieChart);
  }

  fetchLineChartData();
  fetchPieChartData();
});

onBeforeUnmount(() => {
  if (lineChart) {
    lineChart.dispose();
  }
  if (pieChart) {
    pieChart.dispose();
  }
  window.removeEventListener('resize', throttleEchartResize);
});

</script>

<style lang="scss" scoped>
.dashboard {
  height: calc(100% - 100px);
  overflow: auto;
}

.block-wrapper {
  background: white;
  padding: 20px;
  border: solid 1px #ddd;
  border-radius: 5px;
  margin: 20px 0 20px 0;
}

h1 {
  font-size: 16px;
  font-weight: 500;
}

.metrics-wrapper {
  display: flex;
  justify-content: space-between;
  height: 120px;

  .metrics {
    display: flex;
    align-items: center;
    background: white;
    border-radius: 2px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    padding: 10px;
    width: calc(25% - 36px);
    height: 80px;

    h1 {
      display: inline;
      font-weight: bold;
      margin-left: 20px;
      margin-right: 30px;
    }

    span {
      font-size: 36px;
      font-weight: bold;
      color: #8ba74f;
    }
  }
}

.chart-wrapper {
  height: 300px;
  display: grid;
  grid-template-columns: 3fr 1fr;
  gap: 20px;

  h2 {
    font-size: 16px;
    margin-top: 0;
    margin-bottom: 15px;
  }

  .line-chart-wrapper {
    height: 100%;
    width: 100%;
    background-color: white;
    border-radius: 2px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    padding: 25px;
    box-sizing: border-box;
  }

  .pie-chart-wrapper {
    height: 100%;
    width: 100%;
    background-color: white;
    border-radius: 2px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    padding: 25px;
    box-sizing: border-box;
  }
}
</style>
