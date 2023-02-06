class SystemInfo {

    constructor() {
        this._System_Name = 'xmingl';
        this._Timeout = 5;
        this._Api_Url_Map = {
            dev: "/api", prod: "/api"
        };
        this._Api_Url = process.env.NODE_ENV == "production" ? this.Api_Url_Prod : this.Api_Url_Dev;
        this._System_Language = "zh-cn";
    }

    get System_Name() {
        return this._System_Name;
    }

    get Timeout() {
        return this._Timeout;
    }

    get Api_Url() {
        return this._Api_Url;
    }

    get System_Language() {
        return this._System_Language;
    }

    get Api_Url_Dev() {
        return this._Api_Url_Map.dev;
    }

    get Api_Url_Prod() {
        return this._Api_Url_Map.prod;
    }

}

export default new SystemInfo();