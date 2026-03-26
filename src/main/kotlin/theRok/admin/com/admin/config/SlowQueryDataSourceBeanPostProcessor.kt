package theRok.admin.com.admin.config

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class SlowQueryDataSourceBeanPostProcessor(
    private val props: SlowQueryProperties,
    private val slowQueryListener: SlowQueryListener,
) : BeanPostProcessor,
    Ordered {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (!props.enabled) {
            return bean
        }
        if (bean !is DataSource || bean.javaClass.name.startsWith("net.ttddyy.dsproxy")) {
            return bean
        }
        if (beanName != "dataSource") {
            return bean
        }
        return ProxyDataSourceBuilder
            .create(bean)
            .name("slow-query")
            .listener(slowQueryListener)
            .build()
    }

    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE
}
