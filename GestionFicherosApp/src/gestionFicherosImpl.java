
import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class gestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public gestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);

		//que se pueda escribir -> lanzará una excepción

		//que no exista -> lanzará una excepción

		//crear la carpeta -> lanzará una excepción

		
		if(!file.exists()){
			if(carpetaDeTrabajo.canWrite()){
				file.mkdir();	
			}else{
				throw new GestionFicherosException("Error. No tiene permiso de escritura");
			}			
		}else{
			throw new GestionFicherosException("Error. Ya existe una carpeta con ese nombre");
		}
		
		if(file.mkdir()){
			throw new GestionFicherosException("Error. Imposible crear la carpeta "
					+ arg0);
		}
		
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		
		if(!file.exists()){
			if(carpetaDeTrabajo.canWrite()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				throw new GestionFicherosException("Error. No tiene permiso de escritura");
			}
		}else{
			throw new GestionFicherosException("Error. Ya existe un archivo con ese nombre");
		}
		
		actualiza();
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		
		if(file.exists()){
			if(carpetaDeTrabajo.canWrite()){
				file.delete();
			}else{
				throw new GestionFicherosException("Error. No tiene permiso de escritura");
			}
		}else{
			throw new GestionFicherosException("Error. No existe el elemento");
		}
		
		actualiza();
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		//Creo un objeto de tipo SimpleDateFormat para darle el formato pedido a la fecha de última modificación.
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		
		//Controlar que existe. Si no, se lanzará una excepción
		if(!file.exists()){
			throw new GestionFicherosException("Alerta. No existe el fichero o directorio "
					+ file.getAbsolutePath());
		}
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
		if(!file.canRead()){
			throw new GestionFicherosException("Alerta. No se puede acceder al fichero o directorio "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		
		//Título
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		strBuilder.append("Tipo: ");
		//Creo una variable booleana en la que analizo el resultado del metodo isFile(). Si es verdadero muestro que es fichero,
		//si es falso, muestro que es un directorio.
		boolean esFichero = file.isFile();
		if(esFichero){
			strBuilder.append("Fichero");
			strBuilder.append("\n");
			strBuilder.append("Tamaño: ");
			strBuilder.append(file.length()+" bytes");
		}else{
			strBuilder.append("Directorio");
		}
		strBuilder.append("\n");
		
		//Ubicación
		strBuilder.append("Localización: ");
		strBuilder.append(file.getAbsolutePath());
		strBuilder.append("\n");
		
		//Fecha de última modificación
		strBuilder.append("Última modificación: ");
		strBuilder.append(sdf.format(getUltimaModificacion(file.getAbsolutePath())));
		strBuilder.append("\n");
		
		//Si es un fichero oculto o no
		strBuilder.append("Oculto: ");
		//Creo una variable booleana para saber si es oculto o no. Dependiendo de lo que devuelva dicho metodo, aparece Si o No.
		boolean esOculto = file.isHidden();
		if(esOculto){
			strBuilder.append("Si");
		}else{
			strBuilder.append("No");
		}
		strBuilder.append("\n");
		
		//Si es directorio: Espacio libre, espacio disponible, espacio total
		//bytes
		if(!esFichero){
			int elem = 0;
			strBuilder.append("Numero de elementos contenidos: ");
			//Creo un array con los elementos que estan contenidos en el directorio y lo recorro para saber cuantos hay.
			for(int i=0;i<file.list().length;i++){
				elem++;
			}
			strBuilder.append(elem+" elementos");
			strBuilder.append("\n");
			strBuilder.append("Espacio libre del directorio: ");
			strBuilder.append(file.getFreeSpace()+" bytes");
			strBuilder.append("\n");			
			strBuilder.append("Espacio disponible del directorio: ");
			strBuilder.append(file.getUsableSpace()+" bytes");
			strBuilder.append("\n");			
			strBuilder.append("Espacio total del directorio: ");
			strBuilder.append(file.getTotalSpace()+" bytes");
			strBuilder.append("\n");
		}
		
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		File file = new File(arg0);
		Long ultFechaMod;
		//Utilizando el metodo lastModified obtengo la fecha de la ultima modificacion en milisegundos.
		//Luego, le doy el formato correcto usando SimpleDateFormat en el metodo getInformacion.
		ultFechaMod = file.lastModified();
		
		return ultFechaMod;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		File file2 = new File(carpetaDeTrabajo, arg1); 
		
		if(file.exists()){
			if(file.canWrite()){
				file.renameTo(file2);
			}else{
				throw new GestionFicherosException("Error. No tiene permiso de escritura");
			}
		}else{
			throw new GestionFicherosException("Error. No existe el elemento "+arg0);
		}
		
		actualiza();
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
