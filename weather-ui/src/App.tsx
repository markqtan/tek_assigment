import { useEffect, useState } from 'react'
import './App.css'
import { useDispatch, useSelector } from "react-redux";
// import { getWeatheHanlder } from './redux/features/weatherSlice';
import { fetchItems, type Weather} from './redux/features/itemsSlice';


function App() {
  const [zip, setZip] = useState('')
  const dispatch = useDispatch();
  const { items, loading, error } = useSelector((state: any) => {console.log('state:',state); return state.items});
  console.log('weather from store: ', items, loading, error);
  const [errorMessage, setErrorMessage] = useState(error)
  const [weatherResult, setWeatherResult] = useState(null as Weather | null);
  console.log('weatherResult: ', weatherResult);

  const handleChange = (event: any) => {
    setZip(event.target.value); // Update the state with the new input value
    setErrorMessage('');
  };

  const getWeather = () => {
    setErrorMessage('');
    setWeatherResult(null);

    if (!zip) {
      setErrorMessage('');
      return;
    }

    dispatch(fetchItems(zip));
  };

  useEffect(() => {
    setWeatherResult(items.find((item: any) => item.zip === zip) || null);
    setErrorMessage(error);
    console.log('useEffect weather1: ', weatherResult);
  });

  return (
    <>
      <input id="zip" type='text' value={zip} placeholder="Enter zip code..." onChange={handleChange} />
      <button onClick={getWeather} disabled={loading}>Get Temperature</button>
      {weatherResult && (
        <table border={1}>
          <tbody>
          <tr>
            <td>Weather Data Cached: </td><td>{weatherResult.cached ? 'Yes' : 'No'}</td>
          </tr>
          <tr>
            <td>Current Temperature: </td><td>{weatherResult.temp}</td>
          </tr>
          <tr>
            <td>Highest Temperature: </td><td>{weatherResult.tempmax}</td>
          </tr>
          <tr>
            <td>Lowest Temperature: </td><td>{weatherResult.tempmin}</td>
          </tr>
          </tbody>
        </table>
      )}
      <div style={{ color: 'red' }}>{errorMessage}</div>
      <div style={{ color: 'green' }}>{loading?'Loading':null}</div>


    </>
  )
}

export default App
