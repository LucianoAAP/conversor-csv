package com.trybe.conversorcsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Conversor {

  /**
   * Função utilizada apenas para validação da solução do desafio.
   *
   * @param args Não utilizado.
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   * @throws ParseException Caso haja algum problema ao converter datas
   */
  public static void main(String[] args) throws IOException, ParseException {
    File pastaDeEntradas = new File("./entradas/");
    File pastaDeSaidas = new File("./saidas/");
    new Conversor().converterPasta(pastaDeEntradas, pastaDeSaidas);
  }

  /**
   * Converte todos os arquivos CSV da pasta de entradas. Os resultados são gerados
   * na pasta de saídas, deixando os arquivos originais inalterados.
   *
   * @param pastaDeEntradas Pasta contendo os arquivos CSV gerados pela página web.
   * @param pastaDeSaidas Pasta em que serão colocados os arquivos gerados no formato
   *                      requerido pelo subsistema.
   *
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   * @throws ParseException Caso haja algum problema ao converter datas
   */
  public void converterPasta(File pastaDeEntradas, File pastaDeSaidas)
      throws IOException, ParseException {
    if (!pastaDeEntradas.exists()) {
      return;
    }
    if (!pastaDeSaidas.exists()) {
      pastaDeSaidas.mkdir();
    }
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    FileWriter fileWriter = null;
    BufferedWriter bufferedWriter = null;
    for (File file : pastaDeEntradas.listFiles()) {
      fileReader = new FileReader(file);
      bufferedReader = new BufferedReader(fileReader);
      fileWriter = new FileWriter(String.format("%s/%s", pastaDeSaidas, file.getName()), true);
      bufferedWriter = new BufferedWriter(fileWriter);
      String lineContent = bufferedReader.readLine();
      if (lineContent != null) {
        bufferedWriter.write(lineContent);
        bufferedWriter.newLine();
        lineContent = bufferedReader.readLine();
      }
      
      while (lineContent != null) {
        bufferedWriter.write(convertLineData(lineContent));
        bufferedWriter.newLine();
        lineContent = bufferedReader.readLine();
      }      
      bufferedWriter.flush();
      closeReadersAndWriters(fileReader, bufferedReader, fileWriter, bufferedWriter);
    }
  }
  
  
  /**
   * Converte dados da linha.
   * @throws ParseException Caso haja algum problema ao converter datas
   */
  private String convertLineData(String line) throws ParseException {
    String[] lineData =  line.split(",");
    ArrayList<String> result = new ArrayList<>();

    String name = lineData[0];
    name = name.toUpperCase();
    result.add(name);

    String birthDate = lineData[1];
    birthDate = convertBirthDate(birthDate);
    result.add(birthDate);

    String email = lineData[2];
    result.add(email);

    String cpf = lineData[3];
    cpf = convertCpf(cpf);
    
    result.add(cpf);
    return String.join(",", result);
  }
  
  /**
   * Converte data de nascimento.
   * @throws ParseException Caso haja algum problema ao converter datas
   */
  private String convertBirthDate(String birthDate) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date parsedDate = dateFormat.parse(birthDate);
    dateFormat.applyPattern("yyyy-MM-dd");
    return dateFormat.format(parsedDate).toString();
  }
  
  /**
   * Converte cpf.
   */
  private String convertCpf(String cpf) {
    String result = "";
    String[] splittedCpf = cpf.split("");
    for (int index = 0; index <= splittedCpf.length - 1; index += 1) {
      result += splittedCpf[index];
      if (index == 2 || index == 5) {
        result += ".";
      }
      if (index == 8) {
        result += "-";
      }
    }
    return result;
  }

  /**
  * Fecha leitores e escritores.
  */
  private void closeReadersAndWriters(FileReader fileReader, BufferedReader bufferedReader,
      FileWriter fileWriter, BufferedWriter bufferedWriter) {
    try {
      fileReader.close();
      bufferedReader.close();
      fileWriter.close();
      bufferedWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}