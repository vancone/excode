import { MockMethod } from "vite-plugin-mock";

const mock: Array<MockMethod> = [
    {
        url: '/api/passport/admin/v1/user-group',
        method: 'get',
        response: () => {
            return {
                code: 0,
                message: "success",
                data: {
                    list: [
                        {"id":"803986462833610752","displayId":"huaqingwen","name":"花清温","email":"huaqingwen@vancone.com","lastLoginTime":"2023-02-11 01:25:37","createdTime":"2022-12-24 03:15:00"},
                        {"id":"804805372906541056","displayId":"passport_admin","name":"管理员","createdTime":"2022-12-26 09:29:03"},
                        {"id":"814227908207546368","displayId":"buluofen","name":"布洛芬","email":"buluofen@vancone.com","createdTime":"2023-01-21 09:30:32"}
                    ],
                    "pageNo":1,
                    "pageSize":10,
                    "totalCount":3,
                    "totalPage":1
                }
            }
        }
    }
];

export default mock;