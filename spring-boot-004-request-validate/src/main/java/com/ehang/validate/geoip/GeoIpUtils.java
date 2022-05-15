package com.ehang.validate.geoip;

import com.alibaba.fastjson.JSON;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;

import java.io.File;
import java.net.InetAddress;

/**
 * @author LENOVO
 * @title: GenIpUtils
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/5/11 9:57
 */
public class GeoIpUtils {
    private static DatabaseReader reader;

    private static void init() {
        try {
            // 创建 GeoLite2 数据库 Reader
            File database = new File("F:\\web\\GeoLite2-City.mmdb");
            // 读取数据库内容
            reader = new DatabaseReader.Builder(database).build();
        } catch (Exception ex) {

        }
    }


    public static void main(String[] args) throws Exception {
        String ip = "183.19.230.138";
        ip = "104.21.16.245";
        GeoIpUtils.getCityByIP(ip);
        //https://download.maxmind.com/app/geoip_download?edition_id=GeoLite2-City&license_key=O38GA2SviPLnqfF5&suffix=tar.gz
    }


    public static void getCityByIP(String ip) throws Exception {
        if (null == reader) {
            init();
        }

        InetAddress ipAddress = InetAddress.getByName(ip);

        // 获取查询结果
        CityResponse response = reader.city(ipAddress);

        // 获取国家信息
        Country country = response.getCountry();
        System.out.println("国家信息：" + JSON.toJSONString(country));

        // 获取省份
        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println("省份信息：" + JSON.toJSONString(subdivision));

        //城市
        City city = response.getCity();
        System.out.println("城市信息：" + JSON.toJSONString(city));

        // 获取城市
        Location location = response.getLocation();
        System.out.println("经纬度信息：" + JSON.toJSONString(location));
    }
}
