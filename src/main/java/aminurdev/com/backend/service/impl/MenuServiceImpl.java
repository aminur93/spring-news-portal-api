package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Menu;
import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.exception.ResourceNotFoundException;
import aminurdev.com.backend.domain.repository.MenuRepository;
import aminurdev.com.backend.domain.repository.PermissionRepository;
import aminurdev.com.backend.domain.request.MenuRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final PermissionRepository permissionRepository;

    @Override
    public PaginationResponse<Menu> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction,"updatedAt"));

        Page<Menu> menuPage = menuRepository.findAll(pageable);

        List<Menu> menus = menuPage.getContent();

        PaginationResponse<Menu> response = new PaginationResponse<>();

        response.setData(menus);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All menu fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(menuPage.getNumber() + 1);
        meta.setFrom(menuPage.getNumber() * menuPage.getSize() + 1);
        meta.setLastPage(menuPage.getTotalPages());
        meta.setPath("/menu");
        meta.setPerPage(menuPage.getSize());
        meta.setTo((int) menuPage.getTotalElements());
        meta.setTotal((int) menuPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/menu?page=1");
        links.setLast("/menu?page=" + menuPage.getTotalPages());
        if (menuPage.hasPrevious()) {
            links.setPrev("/menu?page=" + menuPage.previousPageable().getPageNumber());
        }
        if (menuPage.hasNext()) {
            links.setNext("/menu?page=" + menuPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Menu> getAllMenus() {

        return menuRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Menu store(MenuRequest request) {

        try{

            Menu menu = new Menu();

            if (request.getPermission_id() != null)
            {
                Permission permission = permissionRepository.findById(request.getPermission_id())
                        .orElseThrow(() -> new RuntimeException("Permission id not found"));

                menu.setPermission(permission);
            }

            menu.setParent_id(request.getParent_id());
            menu.setName_en(request.getName_en());
            menu.setName_bn(request.getName_bn());
            menu.setUrl(request.getUrl());
            menu.setIcon(request.getIcon());
            menu.setHeaderMenu(request.isHeaderMenu());
            menu.setSidebarMenu(request.isSidebarMenu());
            menu.setDropdownMenu(request.isDropdownMenu());
            menu.setChildrenParentMenu(request.getChildrenParentMenu());
            menu.setStatus(request.isStatus());

            return menuRepository.save(menu);

        } catch (Exception e) {

            throw new GlobalException("Error while store menu: " + e.getMessage(), e);
        }
    }

    @Override
    public Menu edit(Integer menuId) {

        return menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu id is not found " + menuId));
    }

    @Override
    public Menu update(Integer menuId, MenuRequest request) {

        try{

            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu id is not found " + menuId));

            if (request.getPermission_id() != null)
            {
                Permission permission = permissionRepository.findById(request.getPermission_id())
                        .orElseThrow(() -> new RuntimeException("Permission id not found"));

                menu.setPermission(permission);
            }

            menu.setParent_id(request.getParent_id());
            menu.setName_en(request.getName_en());
            menu.setName_bn(request.getName_bn());
            menu.setUrl(request.getUrl());
            menu.setIcon(request.getIcon());
            menu.setHeaderMenu(request.isHeaderMenu());
            menu.setSidebarMenu(request.isSidebarMenu());
            menu.setDropdownMenu(request.isDropdownMenu());
            menu.setChildrenParentMenu(request.getChildrenParentMenu());
            menu.setStatus(request.isStatus());

            return menuRepository.save(menu);

        } catch (Exception e) {

            throw new GlobalException("Error while update menu: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy(Integer menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu id is not found " + menuId));
        menuRepository.delete(menu);
    }
}
