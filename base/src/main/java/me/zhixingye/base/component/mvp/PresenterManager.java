package me.zhixingye.base.component.mvp;

import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月05日.
 */
@SuppressWarnings("unchecked")
class PresenterManager {

    private static final Map<IView<?>, IPresenter<?>> PRESENTER_MAP = new HashMap<>();

    @Nullable
    public static synchronized <T extends IPresenter<?>> T getPresenter(IView<T> mvpView) {
        return (T) PRESENTER_MAP.get(mvpView);
    }

    public static synchronized void savePresenter(IView<?> mvpView, IPresenter<?> presenter) {
        if (presenter != null) {
            PRESENTER_MAP.put(mvpView, presenter);
        }
    }

    public static synchronized void removePresenterFromCache(IView<?> mvpView) {
        PRESENTER_MAP.remove(mvpView);
    }

    public static <T extends IPresenter<?>> T createPresenterFromInterfaceGeneric(IView<?> mvpView)
            throws InstantiationException, IllegalAccessException {
        Stack<Type> stack = getInterfaceInheritanceStack(mvpView.getClass(), IView.class);
        if (stack == null || stack.empty()) {
            throw new InstantiationException("Did not find any inheritance relationship of mvp View");
        }

        //栈顶的第一个就是IView接口本身，一定是ParameterizedType
        ParameterizedType iViewType = (ParameterizedType) stack.peek();
        //找到我们要的presenter的继承边界类
        Class<?> presenterBoundsCls = getFirstGenericBounds(iViewType.getActualTypeArguments()[0]);
        if (presenterBoundsCls == null) {
            throw new InstantiationException("Cannot find the generic restriction relationship of presenter from mvp View");
        }

        Class<? extends IPresenter<?>> presenterCls = null;
        //从继承关系栈一层一层找到真正的presenter实现类
        while (presenterCls == null && !stack.empty()) {
            Type type = stack.pop();
            if (!(type instanceof ParameterizedType)) {
                continue;
            }
            ParameterizedType pType = (ParameterizedType) type;
            presenterCls = getPresenterClassFromGeneric(pType, presenterBoundsCls);
        }

        if (presenterCls == null) {
            throw new InstantiationException("Did not find the implementation class related to " +
                    presenterBoundsCls.getName() + " from generic");
        }
        return (T) presenterCls.newInstance();
    }

    private static Stack<Type> getInterfaceInheritanceStack(Type type, Class<?> targetCls) {
        if (!isAssignableFrom(type, targetCls)) {
            return null;
        }
        Stack<Type> stack = new Stack<>();
        Type currentType = type;
        while (currentType != null) {
            stack.push(currentType);
            Class<?> cls = getClassType(currentType);
            if (cls == null) {
                break;
            }
            currentType = null;
            Type[] interfaces = cls.getGenericInterfaces();
            for (Type interfaceType : interfaces) {
                if (isAssignableFrom(interfaceType, targetCls)) {
                    currentType = interfaceType;
                    break;
                }
            }

        }
        return stack;
    }

    private static Class<? extends IPresenter<?>> getPresenterClassFromGeneric(ParameterizedType pType, Class<?> presenterBoundsCls) {
        Type[] actualTypeArguments = pType.getActualTypeArguments();
        if (actualTypeArguments == null) {
            return null;
        }
        for (Type actualType : actualTypeArguments) {
            if (!(actualType instanceof Class)) {
                continue;
            }
            Class<?> possibleClass = (Class<?>) actualType;
            if (presenterBoundsCls.isAssignableFrom(possibleClass)) {
                return (Class<? extends IPresenter<?>>) possibleClass;
            }
        }
        return null;
    }

    private static Class<?> getFirstGenericBounds(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] actualTypeArguments = pType.getActualTypeArguments();
            if (actualTypeArguments.length == 0) {
                return null;
            }
            return getFirstGenericBounds(actualTypeArguments[0]);
        }
        if (type instanceof TypeVariable) {
            return getFirstGenericBounds(((TypeVariable<?>) type).getBounds()[0]);
        }
        return null;
    }


    private static boolean isAssignableFrom(Type type, Class<?> cls) {
        if (type == null) {
            return false;
        }
        if (type instanceof Class) {
            return cls.isAssignableFrom((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return isAssignableFrom(pType.getRawType(), cls);
        }
        return false;
    }

    private static Class<?> getClassType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return getClassType(((ParameterizedType) type).getRawType());
        }
        return null;
    }

}
