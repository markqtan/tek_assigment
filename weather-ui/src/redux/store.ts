import { configureStore } from "@reduxjs/toolkit";
import tourReducer from "./features/tourSlice";
import weatherReducer from "./features/weatherSlice";
import itemsReducer from "./features/itemsSlice";

export default configureStore({
  reducer: {
    tour: tourReducer,
    weather: weatherReducer,
    items: itemsReducer,
  },
});