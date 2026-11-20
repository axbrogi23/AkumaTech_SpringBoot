import {createContext, useContext, useEffect, useState,} from 'react'
import {leerStorage, guardarStorage, eliminarStorage, crearAdminPorDefecto,} from '../utils/storage'

const SesionContext = createContext(null)

const API_URL =
  import.meta.env.VITE_API_URL || 'http://localhost:7778/api/auth'

export function SesionProvider({ children }) {
  const [sesion, setSesion] = useState(function () {
    return leerStorage('sesionActiva', null)
  })

  const [usuarios, setUsuarios] = useState(function () {
    return leerStorage('usuarios', [])
  })

  useEffect(function () {
    crearAdminPorDefecto()
    setUsuarios(leerStorage('usuarios', []))
  }, [])

  async function registrarUsuario(datos) {
    try {
      const respuesta = await fetch(`${API_URL}/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: datos.correo,
          password: datos.password,
        }),
      })

      if (respuesta.status === 409) {
        return {
          ok: false,
          mensaje: 'Este correo ya está registrado.',
        }
      }

      if (!respuesta.ok) {
        let mensaje = 'No fue posible registrar el usuario.'

        try {
          const error = await respuesta.json()

          if (error.detail) {
            mensaje = error.detail
          }
        } catch (error) {
          // El backend no devolvió contenido JSON.
        }

        return {
          ok: false,
          mensaje: mensaje,
        }
      }

      await respuesta.json()

      return {
        ok: true,
        mensaje: '¡Registro exitoso! Ahora puedes iniciar sesión.',
      }
    } catch (error) {
      return {
        ok: false,
        mensaje: 'No fue posible conectar con el backend.',
      }
    }
  }

  async function iniciarSesion(correo, password) {
    try {
      const respuesta = await fetch(`${API_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: correo,
          password: password,
        }),
      })

      if (!respuesta.ok) {
        return {
          ok: false,
          mensaje: 'Correo o contraseña incorrectos.',
        }
      }

      const datos = await respuesta.json()

      const nombreUsuario = correo.split('@')[0]

      const datosSesion = {
        nombre: nombreUsuario,
        correo: correo,
        rol: 'usuario',
        token: datos.token,
      }

      guardarStorage('sesionActiva', datosSesion)
      localStorage.setItem('akuma_token', datos.token)
      setSesion(datosSesion)

      return {
        ok: true,
        mensaje: `¡Inicio de sesión exitoso! Bienvenido ${nombreUsuario}`,
      }
    } catch (error) {
      return {
        ok: false,
        mensaje: 'No fue posible conectar con el backend.',
      }
    }
  }

  function cerrarSesion() {
    eliminarStorage('sesionActiva')
    localStorage.removeItem('akuma_token')
    setSesion(null)
  }

  function esAdmin() {
    return sesion !== null && sesion.rol === 'admin'
  }

  function guardarUsuarios(lista) {
    guardarStorage('usuarios', lista)
    setUsuarios(lista)
  }

  const valor = {
    sesion: sesion,
    usuarios: usuarios,
    iniciarSesion: iniciarSesion,
    cerrarSesion: cerrarSesion,
    registrarUsuario: registrarUsuario,
    esAdmin: esAdmin,
    guardarUsuarios: guardarUsuarios,
  }

  return (
    <SesionContext.Provider value={valor}>
      {children}
    </SesionContext.Provider>
  )
}

export function useSesion() {
  const contexto = useContext(SesionContext)

  if (!contexto) {
    throw new Error(
      'useSesion debe usarse dentro de SesionProvider',
    )
  }

  return contexto
}