import { RouteRecord, RouteRecordRaw } from "vue-router";

export const visibleChildrenFilter = (children: Array<RouteRecordRaw>): Array<RouteRecordRaw> =>
    children.filter(({ meta }) => !meta || !meta.hidden);