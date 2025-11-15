import { createSlice, createAsyncThunk, current } from "@reduxjs/toolkit";
import axios from "axios";
import { act } from "react";

const API = axios.create({ baseURL: "http://localhost:8080" });

interface Weather {
    cached: boolean;
    temp: number;
    tempmax: number;
    tempmin: number;
    zip: string;
}

interface WeatherState {
    // weather: Weather[] | {[index: string]: any};
    // weather: {[key: string] : Weather};
    weather: string[] ;
    loading: boolean;
    error: string | null;
}

export const getWeatheHanlder = createAsyncThunk("weather", async (zip: string, { rejectWithValue }) : Promise<any> => {
    try {
        const response = await API.get("/weather/"  + zip);
        console.log('response data: ', response.data);
        return {zip: zip, data: response.data};
    } catch (err: any) {
        console.log('error: ', err);
        return rejectWithValue({
        message: err.response?.data?.message || 'Failed to fetch user data',
        statusCode: err.response?.status || 500,
      });
    }
});

const weatherSlice = createSlice({
    name: "weather",
    initialState: { weather: new Array<string>(), loading: false, error: null as string | null},
    // initialState: {} as WeatherState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(getWeatheHanlder.fulfilled, (state, action) => {
                const zip = action.payload.zip as string;
                const weatherData = action.payload.data as Weather; 
                // weatherData[zip] = 
                console.log('weatherData', weatherData, typeof state.loading);
                // state.weatherByZip[zip] = action.payload.data;// = Object.assign(state.weatherByZip, weatherData);
                // state.weather = [...state.weather, weatherData];
                // state.weather = weatherData;
                // state.weather = weatherData;
                // (state.weather as []).push(action.payload.data);
                state.weather.push('a');
                state.loading = false;
                state.error = null;
                console.log('state', state);
                // return {
                //     ...state,
                //     zip: action.payload.data,
                //     loading: false,
                //     error: null,
                // }
            })
            .addCase(getWeatheHanlder.rejected, (state, action) => {
                console.log('rejected action: ', action);
                state.weather = {};
                state.loading = false;
                const msg = action.payload as {message: string, statusCode: number};
                state.error = msg.message as string;
                
            })
            .addCase(getWeatheHanlder.pending, (state, action) => {
                console.log('pending action: ', action);
                state.weather = {};
                state.loading = true;
                state.error = null;
                
            })
    },
});

export default weatherSlice.reducer;