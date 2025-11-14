// package com.markqtan.weather_api.aop;

// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.Around;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Before;
// import org.springframework.cache.Cache;
// import org.springframework.cache.Cache.ValueWrapper;
// import org.springframework.cache.CacheManager;
// import org.springframework.stereotype.Component;

// import com.markqtan.weather_api.model.Weather;
// import com.markqtan.weather_api.util.Constants;

// import lombok.RequiredArgsConstructor;

// @Aspect
// @Component
// @RequiredArgsConstructor
// public class CachingAspect {
//     private final CacheManager cacheManager;

//     @Around("execution(* com.markqtan.weather_api.service.WeatherService.getWeatherByZip(..))")
//     public Object cacheBeforeServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//         String method = joinPoint.getSignature().toShortString();
//         Object[] args = joinPoint.getArgs();
//         String zip = args[0].toString();
//         System.out.println("Executing before: " + method + ", zip: " + zip);
//         Weather weather = null;
//         Cache cache = cacheManager.getCache(Constants.WEATHER);
//         System.out.println("cache:" + cache.get(zip));
//         if (cache != null && cache.get(zip) != null) {
//             ValueWrapper wrapper = cache.get(zip);
//             weather = (Weather) wrapper.get();
//             weather.setCached(true);
//             System.out.println("weather: " + weather);
//             return weather;
//         }
//         return joinPoint.proceed(args);

//     }
// }
