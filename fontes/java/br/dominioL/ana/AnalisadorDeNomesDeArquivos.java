package br.dominioL.ana;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class AnalisadorDeNomesDeArquivos {
	private List<String> nomesDeArquivosPermitidos;
	private List<String> nomesDeArquivosPermitidosComExcecao;
	private List<String> nomesDeArquivosPermitidosComAviso;
	private List<String> nomesDeDiretoriosPermitidos;
	private List<String> nomesDeDiretoriosPermitidosComExcecao;
	private List<String> nomesDeDiretoriosPermitidosComAviso;
	private int quantidadeDeDiretorios = 0;
	private int quantidadeDeDiretoriosComExcecao = 0;
	private int quantidadeDeDiretoriosComAviso = 0;
	private int quantidadeDeDiretoriosInvalidos = 0;
	private int quantidadeDeArquivos = 0;
	private int quantidadeDeArquivosComExcecao = 0;
	private int quantidadeDeArquivosComAviso = 0;
	private int quantidadeDeArquivosInvalidos = 0;
	private final String NOME_DA_PASTA_RAIZ = "/home/lucas/lucas";
	
	private AnalisadorDeNomesDeArquivos() {
		final String extensoesValidas = "(dix[.]xml|mp3|mp4|tar[.]gz|[a-z]{1,6})";
		final String parteDeNomesValidos = "([a-z]|[0-9]*[A-Z])*[0-9]*";
		final String nomesValidos = "[a-z]" + parteDeNomesValidos;
		final String nomesValidosComAvisos = "[a-z](" + parteDeNomesValidos + "[A-Z]{2,}" + parteDeNomesValidos + ")+";
		nomesDeArquivosPermitidos = new LinkedList<String>();
		nomesDeArquivosPermitidos.add(nomesValidos + "[.]" + extensoesValidas);
		nomesDeArquivosPermitidosComExcecao = new LinkedList<String>();
		nomesDeArquivosPermitidosComExcecao.add("[A-Z]" + parteDeNomesValidos + "[.]java");
		nomesDeArquivosPermitidosComAviso = new LinkedList<String>();
		nomesDeArquivosPermitidosComAviso.add(nomesValidosComAvisos + "[.]" + extensoesValidas);
		nomesDeDiretoriosPermitidos = new LinkedList<String>();
		nomesDeDiretoriosPermitidos.add(nomesValidos);
		nomesDeDiretoriosPermitidosComExcecao = new LinkedList<String>();
		nomesDeDiretoriosPermitidosComExcecao.add("[.]pessoas");
		nomesDeDiretoriosPermitidosComAviso = new LinkedList<String>();
		nomesDeDiretoriosPermitidosComAviso.add(nomesValidosComAvisos);
		File pastaRaiz = new File(NOME_DA_PASTA_RAIZ);
		entrarNoDiretorio(pastaRaiz);
		mostrarQuantidadeDeArquivosEDiretorios();
	}
	
	private void entrarNoDiretorio(File pastaRaiz) {
		checarNomeDeDiretorio(pastaRaiz);
		for (File arquivo : pastaRaiz.listFiles()) {
			if (arquivo.isDirectory()) {
				entrarNoDiretorio(arquivo);
			} else {
				checarNomeDeArquivo(arquivo);
			}
		}
	}
	
	private void checarNomeDeDiretorio(File diretorio) {
		quantidadeDeDiretorios++;
		String nomeDoDiretorio = diretorio.getName();
		String caminhoDoDiretorio = diretorio.getAbsolutePath();
		if (estaNaLista(nomeDoDiretorio, nomesDeDiretoriosPermitidosComExcecao)) {
			mostrarMensagem("Diretório", "Exceção", nomeDoDiretorio, caminhoDoDiretorio);
			quantidadeDeDiretoriosComExcecao++;
		} else if (estaNaLista(nomeDoDiretorio, nomesDeDiretoriosPermitidosComAviso)) {
			//mostrarMensagem("Diretório", "Aviso", nomeDoDiretorio, caminhoDoDiretorio);
			quantidadeDeDiretoriosComAviso++;
		} else if (!estaNaLista(nomeDoDiretorio, nomesDeDiretoriosPermitidos)) {
			mostrarMensagem("Diretório", "Inválido", nomeDoDiretorio, caminhoDoDiretorio);
			quantidadeDeDiretoriosInvalidos++;
		}
	}
	
	private void checarNomeDeArquivo(File arquivo) {
		quantidadeDeArquivos++;
		String nomeDoArquivo = arquivo.getName();
		String caminhoDoArquivo = arquivo.getAbsolutePath();
		if (estaNaLista(nomeDoArquivo, nomesDeArquivosPermitidosComExcecao)) {
			//mostrarMensagem("Arquivo", "Exceção", nomeDoArquivo, caminhoDoArquivo);
			quantidadeDeArquivosComExcecao++;
		} else if (estaNaLista(nomeDoArquivo, nomesDeArquivosPermitidosComAviso)) {
			//mostrarMensagem("Arquivo", "Aviso", nomeDoArquivo, caminhoDoArquivo);
			quantidadeDeArquivosComAviso++;
		} else 	if (!estaNaLista(nomeDoArquivo, nomesDeArquivosPermitidos)) {
			mostrarMensagem("Arquivo", "Inválido", nomeDoArquivo, caminhoDoArquivo);
			quantidadeDeArquivosInvalidos++;
		}
	}
	
	private boolean estaNaLista(String nome, List<String> lista) {
		boolean combinou = false;
		Iterator<String> iteradorDePermitidos = lista.iterator();
		while (!combinou && iteradorDePermitidos.hasNext()) {
			if (nome.matches(iteradorDePermitidos.next())) {
				combinou = true;
			}
		}
		return combinou;
	}
	
	private void mostrarMensagem(String local, String tipo, String nomeDoArquivo, String caminho) {
		System.out.println(String.format("[%s] [%s] '%s' '%s'", local, tipo, nomeDoArquivo, caminho));
	}
	
	private void mostrarQuantidadeDeArquivosEDiretorios() {
		System.out.println("-----");
		System.out.println("Diretórios:\t" + quantidadeDeDiretorios);
		System.out.println("Diretórios com exceção:\t" + quantidadeDeDiretoriosComExcecao);
		System.out.println("Diretórios com aviso:\t" + quantidadeDeDiretoriosComAviso);
		System.out.println("Diretórios inválidos:\t" + quantidadeDeDiretoriosInvalidos);
		System.out.println("-----");
		System.out.println("Arquivos:\t" + quantidadeDeArquivos);
		System.out.println("Arquivos com exceção:\t" + quantidadeDeArquivosComExcecao);
		System.out.println("Arquivos com aviso:\t" + quantidadeDeArquivosComAviso);
		System.out.println("Arquivos inválidos:\t" + quantidadeDeArquivosInvalidos);
	}
	
	public static void main(String[] argumentos) {
		new AnalisadorDeNomesDeArquivos();
	}
}

