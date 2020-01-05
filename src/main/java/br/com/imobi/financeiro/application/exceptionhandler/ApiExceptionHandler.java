package br.com.imobi.financeiro.application.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.imobi.financeiro.domain.exception.EntityNotFoundException;
import br.com.imobi.financeiro.domain.exception.EntityUseException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_GENERICS_ERRO = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(EntityNotFoundException.class)
	// CONSTRUINDO UMA EXCEÇÃO COM PADRÃO RFC 7807
	public ResponseEntity<?> handlerEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ApiHandlerType apiHandlerType = ApiHandlerType.ENTIDADE_NAO_ENCONTRADA;
		ApiHandler apiHandler = createApiHandlerBuilder(status, apiHandlerType, ex.getMessage()).userMessage(MSG_GENERICS_ERRO).build();
		return handleExceptionInternal(ex, apiHandler, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntityUseException.class)
	// CONSTRUINDO UMA EXCEÇÃO COM PADRÃO RFC 7807
	public ResponseEntity<?> handlerUseException(EntityUseException ex, WebRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		ApiHandlerType apiHandlerType = ApiHandlerType.ENTIDADE_EM_USO;
		ApiHandler apiHandler = createApiHandlerBuilder(status, apiHandlerType, ex.getMessage()).userMessage(MSG_GENERICS_ERRO).build();
		return handleExceptionInternal(ex, apiHandler, new HttpHeaders(), status, request);
	}
	
	// METODO PARA CONSTRUIR UM handle
	private ApiHandler.ApiHandlerBuilder createApiHandlerBuilder(HttpStatus status, ApiHandlerType problemType,String detail) {
		return ApiHandler.builder().timestamp(LocalDateTime.now()).status(status.value()).type(problemType.getUri()).title(problemType.getTitle()).detail(detail);
	}

	@Override
	// LANÇANDO EXCEÇÃO
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,HttpStatus status, WebRequest request) {
		if (body == null) {
			body = ApiHandler.builder().timestamp(LocalDateTime.now()).title(status.getReasonPhrase()).status(status.value()).userMessage(MSG_GENERICS_ERRO).build();
		} else if (body instanceof String) {
			body = ApiHandler.builder().timestamp(LocalDateTime.now()).title((String) body).status(status.value()).userMessage(MSG_GENERICS_ERRO).build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@Override
	// MODIFICANDO UM METODO JA EXISTENTE, ACRESCENTANDO A CAUSA RAIZ
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,HttpHeaders headers, HttpStatus status, WebRequest request) {

		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}

		ApiHandlerType problemType = ApiHandlerType.MENSAGEM_INCOMPREENSIVEL;
		ApiHandler problem = createApiHandlerBuilder(status, problemType, "O corpo da requisição está inválido. Verifique erro de sintaxe.").build();
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	// ACRESCENTANDO A CAUSA RAIZ
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,HttpStatus status, WebRequest request) {
		String path = joinPath(ex.getPath());
		ApiHandlerType apiHandlerType = ApiHandlerType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",path, ex.getValue(), ex.getTargetType().getSimpleName());
		ApiHandler apiHandler = createApiHandlerBuilder(status, apiHandlerType, detail).userMessage(MSG_GENERICS_ERRO).build();
		return handleExceptionInternal(ex, apiHandler, headers, status, request);
	}

	@Override
	//NOT NULL BEAN VALIDATION
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiHandlerType problemType = ApiHandlerType.INVALID_DATA;
	    String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
	    BindingResult bindingResult = ex.getBindingResult();
	   
	    List<ApiHandler.Field> problemFields = bindingResult.getFieldErrors().stream()
	    		.map(fieldError -> {
	    			String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
	    			
	    			return ApiHandler.Field.builder()
	    				.name(fieldError.getField())
	    				.userMessage(message)
	    				.build();
	    		})
	    		.collect(Collectors.toList());
	    
	    ApiHandler apiHandler = createApiHandlerBuilder(status, problemType, detail).userMessage(detail).fields(problemFields).build();
	    return handleExceptionInternal(ex, apiHandler, headers, status, request);
	}
	
	//CONCATENAR ARRAY
	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ApiHandlerType apiHandlerType = ApiHandlerType.SYSTEM_ERROR;
		String detail = MSG_GENERICS_ERRO;
		ex.printStackTrace();
		ApiHandler apiHandler = createApiHandlerBuilder(status, apiHandlerType, detail).build();
		return handleExceptionInternal(ex, apiHandler, new HttpHeaders(), status, request);
	}

	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,HttpStatus status, WebRequest request) {
		String path = joinPath(ex.getPath());
		ApiHandlerType apiHandlerType = ApiHandlerType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' não existe. " + "Corrija ou remova essa propriedade e tente novamente.", path);
		ApiHandler apiHandler = createApiHandlerBuilder(status, apiHandlerType, detail).userMessage(MSG_GENERICS_ERRO).build();
		return handleExceptionInternal(ex, apiHandler, headers, status, request);
	}

}
