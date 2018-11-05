
package com.hsshy.beam.common.base.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hsshy.beam.common.constant.DataBaseConstant;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 抽象实体基类
 * 
 * @author hfBeam
 *
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractEntity<ID> extends Model {

	@TableField(exist = false)
	public long currentPage;
	@TableField(exist = false)
	public long pageSize = DataBaseConstant.PAGE_SIZE;

	public abstract ID getId();

	public abstract void setId(ID id);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.domain.Persistable#isNew()
	 * 
	 */
	public boolean isNew() {

		return null == getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		@SuppressWarnings("unchecked")
		AbstractEntity<ID> that = (AbstractEntity<ID>) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
}
