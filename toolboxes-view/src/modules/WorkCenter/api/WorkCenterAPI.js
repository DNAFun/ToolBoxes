// import axios from '@/utils/axios/AxiosUtils';
import axios from "axios";
import qs from 'qs';
import SystemInfo from "@/SystemInfo";

const baseUrl = SystemInfo.Api_Url + "/WorkCenter/";

export const listByDates = (date = new Date().getTime()) => {
    let data = {millsecord: date};
    return axios.post(baseUrl + "listByDates", qs.stringify(data));
}

export const listDateTaskCount = (date = new Date().getTime()) => {
    let data = {millsecord: date};
    return axios.post(baseUrl + "listDateTaskCount", qs.stringify(data));
}

export default {};