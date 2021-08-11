package io.renren.common.exception;

import io.renren.common.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * 异常处理器
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handlerRRException(RRException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	/**
	 * 校验失败的异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
		R r = new R();
		if (e.getBindingResult().hasErrors()) {
			ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
			r.put("code", 500);
			r.put("msg", objectError.getDefaultMessage());
		}
		return r;
	}

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(IllegalStateException.class)
	public R handlerIllegalStateException(RRException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	/**
	 * 此包下的注解: javax.validation.constraints
	 * 校验提交参数异常
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public R handlerConstraintViolationException(ConstraintViolationException e){
		R r = new R();
		r.put("code", 401);
		r.put("msg", e.getConstraintViolations().iterator().next().getMessageTemplate());

		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return R.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return R.error("没有权限，请联系管理员授权");
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}
}
