import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
const API = axios.create({ baseURL: "http://localhost:5000" });
export const createTour = createAsyncThunk("tour/createTour", async (data, { rejectWithValue }) => {
    try {
        const response = await API.post("/tour", data);
        return response.data;
    } catch (err:any) {
        return rejectWithValue(err.response.data);
    }
});
export const getTours = createAsyncThunk("tour/getTours", async (_, { rejectWithValue }) => {
    try {
        const response = await API.get("/tour");
        return response.data;
    } catch (err:any) {
        return rejectWithValue(err.response.data);
    }
});
export const deleteTour = createAsyncThunk("tour/deleteTour", async (id, { rejectWithValue }) => {
    try {
        await API.delete(`/tour/${id}`);
        return id;
    } catch (err:any) {
        return rejectWithValue(err.response.data);
    }
});
const tourSlice = createSlice({
    name: "tour",
    initialState: { tours: [], loading: false, error: null },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(createTour.fulfilled, (state, action) => {
                state.tours.push(action.payload);
            })
            .addCase(getTours.fulfilled, (state, action) => {
                state.tours = action.payload;
            })
            .addCase(deleteTour.fulfilled, (state, action) => {
                state.tours = state.tours.filter((tour) => tour.id !== action.payload);
            });
    },
});
export default tourSlice.reducer;