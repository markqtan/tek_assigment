import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

interface Weather {
  cached: boolean;
  temp: number;
  tempmax: number;
  tempmin: number;
  zip: string;
}

function App() {
  const [weather, setWeather] = useState({} as Weather)
  const [zip, setZip] = useState('')
  const [error, setError] = useState('')


  const handleChange = (event: any) => {
    setZip(event.target.value); // Update the state with the new input value
  };

  const getWeather = () => {
    setError('');
    setWeather(null);

    fetch('http://localhost:8080/weather/' + zip)
    .then(response => {
      console.log(response.ok);
      if (!response.ok) {
        setWeather(null);
        setError("Failed to fetch weather data")
        return null;
        // throw new Error("Failed to fetch weather data")
      }else {
        console.log(response.json)
        return response.json();}
      }
      
    ).then(json => {
     console.log('json: ', json)
     if(json) {
       setWeather(json)
       setError('')
     }else {
      setError("Failed to fetch weather data")
     }
    }).catch(error => {
      console.log(error)
      setError(error)
      setWeather(null);
    })
  };

  return (
    <>
      <input id="zip" type='text' value={zip} placeholder="Enter zip code..." onChange={handleChange}/> 
      <button onClick={getWeather}>Get Temperature</button>
      {weather && (
      <table border={1}>
        <tr>
          <td>Weather Data Cached: </td><td>{weather.cached?'Yes':'No'}</td>
        </tr>
        <tr>
          <td>Current Temperature: </td><td>{weather.temp}</td>
        </tr>
        <tr>
          <td>Highest Temperature: </td><td>{weather.tempmax}</td>
        </tr>
        <tr>
          <td>Lowest Temperature: </td><td>{weather.tempmin}</td>
        </tr>
      </table>
      )}
        <tr>
          <td style={{ color: 'red' }}>{error}</td>
        </tr>
      
       
    </>
  )
}

export default App
