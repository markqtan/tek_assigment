import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from "axios";

const API = axios.create({ baseURL: "http://localhost:8080" });


// Assume you have an async thunk defined
export const fetchItems = createAsyncThunk('items/fetchItems', async (zip: string, { rejectWithValue }) => {
  try {
        const response = await API.get("/weather/"  + zip);
        console.log('response data: ', response.data);
        // return {zip: zip, data: response.data};
        return {...response.data, zip};
    } catch (err: any) {
        console.log('error: ', err);
        return rejectWithValue({
        message: err.response?.data?.message || 'Failed to fetch user data',
        statusCode: err.response?.status || 500,
      });
    }
});

export interface Weather {
    cached: boolean;
    temp: number;
    tempmax: number;
    tempmin: number;
    zip: string;
}

interface ItemsState {
  items: Weather[];
  status: 'idle' | 'loading' | 'succeeded' | 'failed';
  error: string | null;
  loading: boolean;
}

const initialState: ItemsState = {
  items: [],
  status: 'idle',
  error: null,
  loading: false,
};

const itemsSlice = createSlice({
  name: 'items',
  initialState,
  reducers: {},
  extraReducers: (builder) => { // Use the builder callback
    builder
      .addCase(fetchItems.fulfilled, (state, action) => {
        // Immer (built into RTK) allows direct mutation
        state.status = 'succeeded';
        const filtered = state.items.filter(item => item.zip !== action.payload.zip);
        filtered.push(action.payload); // This should work correctly
        state.items = filtered;
        state.error = null;
        state.loading = false;
      })
      .addCase(fetchItems.pending, (state, action) => {
        state.status = 'loading';
        state.error = null;
        state.loading = true;
      })
      .addCase(fetchItems.rejected, (state, action) => {
        state.status = 'failed';
        // state.error = action.error.message || 'Something went wrong';
        const msg = action.payload as {message: string, statusCode: number};
        state.error = msg.message as string;
        state.loading = false;
      });
  },
});

export default itemsSlice.reducer;
