import * as echarts from 'echarts/core';
import { LineSeriesOption, PieSeriesOption } from 'echarts/charts';
import {
    TooltipComponentOption,
    GridComponentOption,
    MarkLineComponentOption,
} from 'echarts/components';

type LineOption = echarts.ComposeOption<
    LineSeriesOption | TooltipComponentOption | GridComponentOption | MarkLineComponentOption>;

type PieOption = echarts.ComposeOption<
    PieSeriesOption | TooltipComponentOption | GridComponentOption>;

const grid = {
    top: 10,
    bottom: 10,
    left: 0,
    right: 20,
    containLabel: true,
};

function getLineAreaColor(startColor: string, endColor: string) {
    return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        {
            offset: 0,
            color: startColor,
        },
        {
            offset: 1,
            color: endColor,
        }
    ])
}

export function generateLineChartOptions(
    dateList: Array<string>,
    metric1Data: Array<number>,
    metric2Data: Array<number>,
): LineOption {
    return {
        grid: {
            ...grid,
            top: 30,
            right: 30,
        },
        tooltip: {
            trigger: 'axis',
        },
        legend: {
            right: 0,
            data: ['Metric 1', 'Metric 2'],
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: dateList,
        },
        yAxis: {
            type: 'value',
        },
        series: [
            {
                name: 'Metric 1',
                type: 'line',
                lineStyle: {
                    color: '#6065EE',
                },
                itemStyle: {
                    color: '#6065EE',
                },
                symbol: 'none',
                symbolSize: 6,
                data: metric1Data,
                smooth: true,
                areaStyle: {
                    color: getLineAreaColor('#6065EE', '#DEE2FA'),
                }
            },
            {
                name: 'Metric 2',
                type: 'line',
                lineStyle: {
                    color: '#4DE2C0',
                },
                itemStyle: {
                    color: '#4DE2C0',
                },
                symbol: 'none',
                symbolSize: 6,
                data: metric2Data,
                smooth: true,
                areaStyle: {
                    color: getLineAreaColor('#4DE2C0', '#8ED5E5'),
                }
            }
        ]
    }
}

export function generatePieOptions(
    data: Array<{ name: string; value: number }>
): PieOption {
    return {
        tooltip: {
            trigger: 'item',
        },
        legend: {
            top: 'middle',
            right: 0,
            orient: 'vertical',
            formatter(key: string) {
                const value = data.find(({ name }) => name == key)?.value ?? 0;
                return `${key} (${value})`;
            },
        },
        color: [ '#0EC2C5', '#0082FE', '#FEC248', '#E0E3E9', '#36C6FE' ],
        series: [
            {
                name: 'Pie Chart Demo',
                type: 'pie',
                radius: ['40%', '60%'],
                avoidLabelOverlap: false,
                label: {
                    show: false,
                },
                center: ['25%', '50%'],
                data,
            },
        ],
    };
}