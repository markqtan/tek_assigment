import { configureStore } from "@reduxjs/toolkit";
import tourReducer from "./features/tourSlice";
import weatherReducer from "./features/weatherSlice";
import itemsReducer from "./features/itemsSlice";

export const store = configureStore({
  reducer: {
    tour: tourReducer,
    weather: weatherReducer,
    items: itemsReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;